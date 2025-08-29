package com.example.perks.listeners;

import com.example.perks.PerksPlugin;
import com.example.perks.managers.AdminManager;
import com.example.perks.managers.EffectsManager;
import com.example.perks.model.Perk;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class AdminMenuListener implements Listener {
    
    private final PerksPlugin plugin;
    private final AdminManager adminManager;
    private final EffectsManager effectsManager;
    
    public AdminMenuListener(PerksPlugin plugin, AdminManager adminManager, EffectsManager effectsManager) {
        this.plugin = plugin;
        this.adminManager = adminManager;
        this.effectsManager = effectsManager;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = ChatColor.stripColor(event.getView().getTitle()).toLowerCase();
        
        if (!title.contains("admin") && !title.contains("manage") && !title.contains("configuration")) {
            return;
        }
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player admin)) {
            return;
        }
        
        if (!admin.hasPermission("nitroperks.admin")) {
            admin.closeInventory();
            admin.sendMessage(plugin.getPrefix() + "§cYou don't have permission to use admin features.");
            return;
        }
        
        if (event.getCurrentItem() == null) return;
        
        effectsManager.playMenuClickEffect(admin);
        
        String menuType = adminManager.getMenuType(title);
        int slot = event.getRawSlot();
        
        switch (menuType) {
            case "main" -> handleMainAdminMenuClick(admin, slot);
            case "player_management" -> handlePlayerManagementClick(admin, slot);
            case "player_perks" -> handlePlayerPerksClick(admin, slot, event.getClick());
            case "perk_config" -> handlePerkConfigClick(admin, slot);
        }
    }
    
    private void handleMainAdminMenuClick(Player admin, int slot) {
        switch (slot) {
            case 10 -> adminManager.openPlayerManagement(admin);
            case 12 -> adminManager.openPerkConfiguration(admin);
            case 14 -> showStatistics(admin);
            case 16 -> reloadConfiguration(admin);
            case 22 -> admin.closeInventory();
        }
    }
    
    private void handlePlayerManagementClick(Player admin, int slot) {
        if (slot == 49) { // Back button
            adminManager.openMainAdminMenu(admin);
            return;
        }
        
        if (slot < 45) {
            Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if (slot < onlinePlayers.length) {
                Player target = onlinePlayers[slot];
                adminManager.openPlayerPerksMenu(admin, target);
            }
        }
    }
    
    private void handlePlayerPerksClick(Player admin, int slot, ClickType clickType) {
        if (slot == 49) { // Back button
            adminManager.openPlayerManagement(admin);
            return;
        }
        
        if (slot >= 45) return;
        
        // Extract target player name from inventory title
        String title = admin.getOpenInventory().getTitle();
        String targetName = ChatColor.stripColor(title).replace("Manage ", "");
        Player target = Bukkit.getPlayer(targetName);
        
        if (target == null) {
            admin.sendMessage(plugin.getPrefix() + "§cTarget player is no longer online.");
            adminManager.openPlayerManagement(admin);
            return;
        }
        
        Perk[] perks = Perk.values();
        if (slot >= perks.length) return;
        
        Perk perk = perks[slot];
        boolean hasUnlocked = plugin.getPerkManager().hasUnlocked(target, perk);
        
        if (clickType == ClickType.LEFT) {
            if (hasUnlocked) {
                // Toggle perk
                adminManager.togglePerkForPlayer(target, perk);
                admin.sendMessage(plugin.getPrefix() + "§aToggled " + perk.getKey() + " for " + target.getName());
            } else {
                // Give perk
                adminManager.givePerkToPlayer(target, perk);
                admin.sendMessage(plugin.getPrefix() + "§aGave " + perk.getKey() + " to " + target.getName());
                target.sendMessage(plugin.getPrefix() + "§aYou have been given the " + perk.getKey() + " perk by an admin!");
            }
        } else if (clickType == ClickType.RIGHT && hasUnlocked) {
            // Remove perk
            adminManager.removePerkFromPlayer(target, perk);
            admin.sendMessage(plugin.getPrefix() + "§cRemoved " + perk.getKey() + " from " + target.getName());
            target.sendMessage(plugin.getPrefix() + "§cThe " + perk.getKey() + " perk has been removed by an admin.");
        }
        
        // Refresh the menu
        adminManager.openPlayerPerksMenu(admin, target);
    }
    
    private void handlePerkConfigClick(Player admin, int slot) {
        if (slot == 49) { // Back button
            adminManager.openMainAdminMenu(admin);
            return;
        }
        
        if (slot >= 45) return;
        
        Perk[] perks = Perk.values();
        if (slot >= perks.length) return;
        
        Perk perk = perks[slot];
        
        // For simplicity, we'll just show current price and suggest using commands
        admin.closeInventory();
        admin.sendMessage(plugin.getPrefix() + "§eCurrent price for " + perk.getKey() + ": §a$" + plugin.getPrice(perk));
        admin.sendMessage(plugin.getPrefix() + "§7Use §f/perks setprice " + perk.getKey() + " <amount> §7to change the price.");
    }
    
    private void showStatistics(Player admin) {
        admin.closeInventory();
        
        int totalPlayers = Bukkit.getOfflinePlayers().length;
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        
        admin.sendMessage("§8§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        admin.sendMessage("§6§l                    NITROPERKS STATISTICS");
        admin.sendMessage("§8§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        admin.sendMessage("");
        admin.sendMessage("§7Total Players: §f" + totalPlayers);
        admin.sendMessage("§7Online Players: §a" + onlinePlayers);
        admin.sendMessage("§7Available Perks: §b" + Perk.values().length);
        admin.sendMessage("");
        
        // Show most popular perks (based on online players)
        admin.sendMessage("§6§lOnline Player Perk Usage:");
        for (Perk perk : Perk.values()) {
            long activeCount = Bukkit.getOnlinePlayers().stream()
                .mapToLong(p -> plugin.getPerkManager().isActive(p, perk) ? 1 : 0)
                .sum();
            
            if (activeCount > 0) {
                admin.sendMessage("§7" + formatPerkName(perk.getKey()) + ": §a" + activeCount + " players");
            }
        }
        
        admin.sendMessage("");
        admin.sendMessage("§8§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
    
    private void reloadConfiguration(Player admin) {
        admin.closeInventory();
        plugin.reload();
        admin.sendMessage(plugin.getPrefix() + plugin.getMessage("reload-success", Map.of()));
    }
    
    private String formatPerkName(String key) {
        return java.util.Arrays.stream(key.split("-"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
            .reduce((a, b) -> a + " " + b)
            .orElse(key);
    }
}
