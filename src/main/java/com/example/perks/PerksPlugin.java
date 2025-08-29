package com.example.perks;

import com.example.perks.commands.PerkShopCommand;
import com.example.perks.commands.PerksCommand;
import com.example.perks.listeners.AdminMenuListener;
import com.example.perks.listeners.PerkEffectListener;
import com.example.perks.listeners.PerkMenuListener;
import com.example.perks.listeners.PlayerDataListener;
import com.example.perks.managers.AdminManager;
import com.example.perks.managers.DatabaseManager;
import com.example.perks.managers.EffectsManager;
import com.example.perks.managers.MenuManager;
import com.example.perks.managers.PerkManager;
import com.example.perks.model.Perk;
import com.example.perks.utils.VersionCompatibility;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class PerksPlugin extends JavaPlugin {
    private DatabaseManager databaseManager;
    private PerkManager perkManager;
    private MenuManager menuManager;
    private EffectsManager effectsManager;
    private AdminManager adminManager;
    private FileConfiguration messages;
    private Economy economy;
    private String prefix;

    @Override
    public void onEnable() {
        // Check version compatibility first
        VersionCompatibility.logCompatibilityInfo();
        
        if (!VersionCompatibility.IS_SUPPORTED_VERSION) {
            getLogger().severe("=== VERSION WARNING ===");
            getLogger().severe("This version of Minecraft may not be fully supported!");
            getLogger().severe("Recommended versions: 1.20.x - 1.21.8");
            getLogger().severe("Current version may cause issues or missing features.");
            getLogger().severe("======================");
        }
        
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("menus.yml", false);
        messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        prefix = messages.getString("prefix", "");

        if (!setupEconomy()) {
            getServer().getConsoleSender().sendMessage(getMessage("vault-missing", Map.of()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        effectsManager = new EffectsManager(this);
        perkManager = new PerkManager(this, effectsManager);
        menuManager = new MenuManager(this, perkManager);
        adminManager = new AdminManager(this, perkManager);

        getCommand("perks").setExecutor(new PerksCommand(this, perkManager, menuManager));
        getCommand("perkshop").setExecutor(new PerkShopCommand(perkManager, menuManager));
        getServer().getPluginManager().registerEvents(new PerkMenuListener(perkManager, menuManager, effectsManager), this);
        getServer().getPluginManager().registerEvents(new PerkEffectListener(perkManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDataListener(this), this);
        getServer().getPluginManager().registerEvents(new AdminMenuListener(this, adminManager, effectsManager), this);

        getServer().getConsoleSender().sendMessage(prefix + getMessage("plugin-enabled", Map.of("version", getDescription().getVersion())));
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        getServer().getConsoleSender().sendMessage(prefix + getMessage("plugin-disabled", Map.of()));
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public String getMessage(String key, Map<String, String> placeholders) {
        String msg = messages.getString(key, key);
        if (msg == null) msg = key;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            msg = msg.replace("{" + e.getKey() + "}", e.getValue());
        }
        return msg.replace('&', '§');
    }

    public String getPrefix() {
        return prefix.replace('&', '§');
    }

    public double getPrice(Perk perk) {
        return getConfig().getDouble("prices." + perk.getKey(), 0);
    }

    public void reload() {
        reloadConfig();
        messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        prefix = messages.getString("prefix", "");
        if (menuManager != null) {
            menuManager.loadMenuConfig();
        }
        if (effectsManager != null) {
            effectsManager.loadConfig();
        }
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
    
    public EffectsManager getEffectsManager() {
        return effectsManager;
    }
    
    public FileConfiguration getMessages() {
        return messages;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public PerkManager getPerkManager() {
        return perkManager;
    }
}
