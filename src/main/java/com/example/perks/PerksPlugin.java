package com.example.perks;

import com.example.perks.commands.PerkShopCommand;
import com.example.perks.commands.PerksCommand;
import com.example.perks.listeners.PerkEffectListener;
import com.example.perks.listeners.PerkMenuListener;
import com.example.perks.managers.DatabaseManager;
import com.example.perks.managers.PerkManager;
import com.example.perks.model.Perk;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class PerksPlugin extends JavaPlugin {
    private DatabaseManager databaseManager;
    private PerkManager perkManager;
    private FileConfiguration messages;
    private Economy economy;
    private String prefix;

    @Override
    public void onEnable() {
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

        perkManager = new PerkManager(this);

        getCommand("perks").setExecutor(new PerksCommand(this, perkManager));
        getCommand("perkshop").setExecutor(new PerkShopCommand(perkManager));
        getServer().getPluginManager().registerEvents(new PerkMenuListener(perkManager), this);
        getServer().getPluginManager().registerEvents(new PerkEffectListener(perkManager), this);

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
    }
}
