package com.example.perks.managers;

import com.example.perks.PerksPlugin;
import com.example.perks.model.Perk;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {

    private final PerksPlugin plugin;
    private Connection connection;
    private final Map<UUID, Set<String>> playerPerksCache = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> activePerksCache = new ConcurrentHashMap<>();

    public DatabaseManager(PerksPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        String host = plugin.getConfig().getString("mysql.host");
        int port = plugin.getConfig().getInt("mysql.port");
        String database = plugin.getConfig().getString("mysql.database");
        String username = plugin.getConfig().getString("mysql.username");
        String password = plugin.getConfig().getString("mysql.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
        try {
            connection = DriverManager.getConnection(url, username, password);
            plugin.getLogger().info("Connected to MySQL database");
            createTables();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not connect to MySQL: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create player_perks table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS player_perks (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    player_uuid VARCHAR(36) NOT NULL,
                    perk_key VARCHAR(50) NOT NULL,
                    unlocked BOOLEAN DEFAULT TRUE,
                    active BOOLEAN DEFAULT FALSE,
                    unlock_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    level INT DEFAULT 1,
                    usage_count BIGINT DEFAULT 0,
                    UNIQUE KEY unique_player_perk (player_uuid, perk_key)
                )
            """);
            
            // Create player_stats table for tracking
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS player_stats (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    player_uuid VARCHAR(36) NOT NULL,
                    total_perks_owned INT DEFAULT 0,
                    total_money_spent DECIMAL(15,2) DEFAULT 0.00,
                    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY unique_player (player_uuid)
                )
            """);
            
            plugin.getLogger().info("Database tables created/verified successfully");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating tables: " + e.getMessage());
        }
    }

    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        
        // Load asynchronously to avoid blocking main thread
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Set<String> unlockedPerks = new HashSet<>();
                    Set<String> activePerks = new HashSet<>();
                    
                    String query = "SELECT perk_key, unlocked, active FROM player_perks WHERE player_uuid = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, uuid.toString());
                        ResultSet rs = stmt.executeQuery();
                        
                        while (rs.next()) {
                            String perkKey = rs.getString("perk_key");
                            boolean unlocked = rs.getBoolean("unlocked");
                            boolean active = rs.getBoolean("active");
                            
                            if (unlocked) {
                                unlockedPerks.add(perkKey);
                                if (active) {
                                    activePerks.add(perkKey);
                                }
                            }
                        }
                    }
                    
                    // Update cache on main thread
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            playerPerksCache.put(uuid, unlockedPerks);
                            activePerksCache.put(uuid, activePerks);
                            
                            // Apply active perks
                            for (String perkKey : activePerks) {
                                Perk perk = Perk.fromString(perkKey);
                                if (perk != null) {
                                    perk.apply(player);
                                }
                            }
                        }
                    }.runTask(plugin);
                    
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error loading player data: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void savePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        Set<String> unlockedPerks = playerPerksCache.get(uuid);
        Set<String> activePerks = activePerksCache.get(uuid);
        
        if (unlockedPerks == null) return;
        
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Update player stats
                    String updateStats = """
                        INSERT INTO player_stats (player_uuid, total_perks_owned, last_seen) 
                        VALUES (?, ?, NOW()) 
                        ON DUPLICATE KEY UPDATE 
                        total_perks_owned = VALUES(total_perks_owned), 
                        last_seen = VALUES(last_seen)
                    """;
                    
                    try (PreparedStatement stmt = connection.prepareStatement(updateStats)) {
                        stmt.setString(1, uuid.toString());
                        stmt.setInt(2, unlockedPerks.size());
                        stmt.executeUpdate();
                    }
                    
                    // Update perk states
                    for (String perkKey : unlockedPerks) {
                        boolean isActive = activePerks != null && activePerks.contains(perkKey);
                        
                        String upsertPerk = """
                            INSERT INTO player_perks (player_uuid, perk_key, unlocked, active) 
                            VALUES (?, ?, TRUE, ?) 
                            ON DUPLICATE KEY UPDATE 
                            active = VALUES(active)
                        """;
                        
                        try (PreparedStatement stmt = connection.prepareStatement(upsertPerk)) {
                            stmt.setString(1, uuid.toString());
                            stmt.setString(2, perkKey);
                            stmt.setBoolean(3, isActive);
                            stmt.executeUpdate();
                        }
                    }
                    
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error saving player data: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public Set<String> getUnlockedPerks(UUID uuid) {
        return playerPerksCache.getOrDefault(uuid, new HashSet<>());
    }

    public Set<String> getActivePerks(UUID uuid) {
        return activePerksCache.getOrDefault(uuid, new HashSet<>());
    }

    public void addPerk(UUID uuid, String perkKey) {
        playerPerksCache.computeIfAbsent(uuid, k -> new HashSet<>()).add(perkKey);
        
        // Save to database asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String query = """
                        INSERT INTO player_perks (player_uuid, perk_key, unlocked) 
                        VALUES (?, ?, TRUE) 
                        ON DUPLICATE KEY UPDATE unlocked = TRUE
                    """;
                    
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, uuid.toString());
                        stmt.setString(2, perkKey);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error adding perk to database: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void removePerk(UUID uuid, String perkKey) {
        Set<String> perks = playerPerksCache.get(uuid);
        if (perks != null) {
            perks.remove(perkKey);
        }
        
        Set<String> activePerks = activePerksCache.get(uuid);
        if (activePerks != null) {
            activePerks.remove(perkKey);
        }
        
        // Update database asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String query = "DELETE FROM player_perks WHERE player_uuid = ? AND perk_key = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, uuid.toString());
                        stmt.setString(2, perkKey);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error removing perk from database: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void setPerkActive(UUID uuid, String perkKey, boolean active) {
        if (active) {
            activePerksCache.computeIfAbsent(uuid, k -> new HashSet<>()).add(perkKey);
        } else {
            Set<String> activePerks = activePerksCache.get(uuid);
            if (activePerks != null) {
                activePerks.remove(perkKey);
            }
        }
        
        // Update database asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String query = "UPDATE player_perks SET active = ? WHERE player_uuid = ? AND perk_key = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setBoolean(1, active);
                        stmt.setString(2, uuid.toString());
                        stmt.setString(3, perkKey);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error updating perk state: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void recordPurchase(UUID uuid, String perkKey, double price) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Update player stats
                    String updateStats = """
                        INSERT INTO player_stats (player_uuid, total_money_spent) 
                        VALUES (?, ?) 
                        ON DUPLICATE KEY UPDATE 
                        total_money_spent = total_money_spent + VALUES(total_money_spent)
                    """;
                    
                    try (PreparedStatement stmt = connection.prepareStatement(updateStats)) {
                        stmt.setString(1, uuid.toString());
                        stmt.setDouble(2, price);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error recording purchase: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        // Save all cached data before disconnecting
        for (UUID uuid : playerPerksCache.keySet()) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                savePlayerData(player);
            }
        }
        
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("MySQL connection closed");
            } catch (SQLException e) {
                plugin.getLogger().severe("Could not close MySQL connection: " + e.getMessage());
            }
        }
    }
}
