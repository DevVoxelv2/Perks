package com.example.perks.managers;

import com.example.perks.PerksPlugin;
import com.example.perks.model.Perk;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

public class PerkManager {
    private final PerksPlugin plugin;
    private final EffectsManager effectsManager;
    private final DatabaseManager databaseManager;

    public PerkManager(PerksPlugin plugin, EffectsManager effectsManager) {
        this.plugin = plugin;
        this.effectsManager = effectsManager;
        this.databaseManager = plugin.getDatabaseManager();
    }

    public boolean isActive(Player p, Perk perk) {
        return databaseManager.getActivePerks(p.getUniqueId()).contains(perk.getKey());
    }

    public boolean hasUnlocked(Player p, Perk perk) {
        return databaseManager.getUnlockedPerks(p.getUniqueId()).contains(perk.getKey());
    }

    public void addPerk(Player p, Perk perk) {
        databaseManager.addPerk(p.getUniqueId(), perk.getKey());
    }

    public void removePerk(Player p, Perk perk) {
        databaseManager.removePerk(p.getUniqueId(), perk.getKey());
        deactivate(p, perk);
    }

    public void toggle(Player p, Perk perk) {
        if (!p.hasPermission(perk.getPermission())) {
            p.sendMessage(plugin.getPrefix() + plugin.getMessage("no-permission", Map.of()));
            return;
        }
        if (!hasUnlocked(p, perk)) {
            p.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-perk", Map.of("perk", perk.getKey())));
            return;
        }
        
        boolean currentlyActive = isActive(p, perk);
        if (currentlyActive) {
            databaseManager.setPerkActive(p.getUniqueId(), perk.getKey(), false);
            perk.remove(p);
            p.sendMessage(plugin.getPrefix() + plugin.getMessage("toggle-off", Map.of("perk", perk.getKey())));
            effectsManager.showPerkStatusChange(p, formatPerkName(perk.getKey()), false);
        } else {
            databaseManager.setPerkActive(p.getUniqueId(), perk.getKey(), true);
            perk.apply(p);
            p.sendMessage(plugin.getPrefix() + plugin.getMessage("toggle-on", Map.of("perk", perk.getKey())));
            effectsManager.showPerkStatusChange(p, formatPerkName(perk.getKey()), true);
        }
    }

    public void deactivate(Player p, Perk perk) {
        if (isActive(p, perk)) {
            databaseManager.setPerkActive(p.getUniqueId(), perk.getKey(), false);
            perk.remove(p);
        }
    }

    public void openOverview(Player player) {
        // This method is now handled by MenuManager
        // Keeping for backward compatibility
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&9Perks"));
        int i = 0;
        for (Perk perk : Perk.values()) {
            inv.setItem(i++, perk.createIcon());
        }
        player.openInventory(inv);
    }

    public void openShop(Player player) {
        // This method is now handled by MenuManager
        // Keeping for backward compatibility
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&bPerk Shop"));
        int i = 0;
        for (Perk perk : Perk.values()) {
            inv.setItem(i++, perk.createIcon());
        }
        player.openInventory(inv);
    }

    public boolean purchase(Player player, Perk perk) {
        if (!player.hasPermission(perk.getPermission())) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("no-permission", Map.of()));
            return false;
        }
        if (hasUnlocked(player, perk)) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("perk-already", Map.of("player", player.getName(), "perk", perk.getKey())));
            return false;
        }
        Economy eco = plugin.getEconomy();
        if (eco == null) {
            return false;
        }
        double price = plugin.getPrice(perk);
        if (!eco.has(player, price)) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("not-enough-money", Map.of("perk", perk.getKey())));
            effectsManager.playPurchaseFailEffects(player);
            return false;
        }
        eco.withdrawPlayer(player, price);
        addPerk(player, perk);
        databaseManager.recordPurchase(player.getUniqueId(), perk.getKey(), price);
        player.sendMessage(plugin.getPrefix() + plugin.getMessage("purchase-success", Map.of("perk", perk.getKey(), "price", String.valueOf((int) price))));
        effectsManager.playPurchaseSuccessEffects(player);
        return true;
    }
    
    private String formatPerkName(String key) {
        return Arrays.stream(key.split("-"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
            .reduce((a, b) -> a + " " + b)
            .orElse(key);
    }
}
