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
    private final Map<UUID, Set<Perk>> unlocked = new HashMap<>();
    private final Map<UUID, Set<Perk>> active = new HashMap<>();

    public PerkManager(PerksPlugin plugin) {
        this.plugin = plugin;
    }

    private Set<Perk> getUnlocked(Player p) {
        return unlocked.computeIfAbsent(p.getUniqueId(), k -> new HashSet<>());
    }

    private Set<Perk> getActive(Player p) {
        return active.computeIfAbsent(p.getUniqueId(), k -> new HashSet<>());
    }

    public boolean isActive(Player p, Perk perk) {
        return getActive(p).contains(perk);
    }

    public boolean hasUnlocked(Player p, Perk perk) {
        return getUnlocked(p).contains(perk);
    }

    public void addPerk(Player p, Perk perk) {
        getUnlocked(p).add(perk);
    }

    public void removePerk(Player p, Perk perk) {
        getUnlocked(p).remove(perk);
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
        Set<Perk> set = getActive(p);
        if (set.contains(perk)) {
            set.remove(perk);
            perk.remove(p);
            p.sendMessage(plugin.getPrefix() + plugin.getMessage("toggle-off", Map.of("perk", perk.getKey())));
        } else {
            set.add(perk);
            perk.apply(p);
            p.sendMessage(plugin.getPrefix() + plugin.getMessage("toggle-on", Map.of("perk", perk.getKey())));
        }
    }

    public void deactivate(Player p, Perk perk) {
        Set<Perk> set = getActive(p);
        if (set.remove(perk)) {
            perk.remove(p);
        }
    }

    public void openOverview(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&9Perks"));
        int i = 0;
        for (Perk perk : Perk.values()) {
            inv.setItem(i++, perk.createIcon());
        }
        player.openInventory(inv);
    }

    public void openShop(Player player) {
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
            return false;
        }
        eco.withdrawPlayer(player, price);
        addPerk(player, perk);
        player.sendMessage(plugin.getPrefix() + plugin.getMessage("purchase-success", Map.of("perk", perk.getKey(), "price", String.valueOf((int) price))));
        return true;
    }
}
