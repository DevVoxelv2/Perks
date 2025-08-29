package com.example.perks.commands;

import com.example.perks.PerksPlugin;
import com.example.perks.managers.AdminManager;
import com.example.perks.managers.MenuManager;
import com.example.perks.managers.PerkManager;
import com.example.perks.model.Perk;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class PerksCommand implements CommandExecutor {

    private final PerksPlugin plugin;
    private final PerkManager perkManager;
    private final MenuManager menuManager;
    private final AdminManager adminManager;

    public PerksCommand(PerksPlugin plugin, PerkManager perkManager, MenuManager menuManager) {
        this.plugin = plugin;
        this.perkManager = perkManager;
        this.menuManager = menuManager;
        this.adminManager = new AdminManager(plugin, perkManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }
        if (args.length == 0) {
            menuManager.openMainMenu(player);
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "setprice":
                if (!sender.hasPermission("boosterperks.admin")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("admin-no-permission", Map.of()));
                    return true;
                }
                if (args.length < 3) return true;
                Perk perk = Perk.fromString(args[1]);
                if (perk == null) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-perk", Map.of("perk", args[1])));
                    return true;
                }
                double price;
                try {
                    price = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    return true;
                }
                plugin.getConfig().set("prices." + perk.getKey(), price);
                plugin.saveConfig();
                sender.sendMessage(plugin.getPrefix() + plugin.getMessage("setprice-success", Map.of("perk", perk.getKey(), "price", args[2])));
                return true;
            case "add":
                if (!sender.hasPermission("boosterperks.admin")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("admin-no-permission", Map.of()));
                    return true;
                }
                if (args.length < 3) return true;
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-player", Map.of("player", args[1])));
                    return true;
                }
                perk = Perk.fromString(args[2]);
                if (perk == null) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-perk", Map.of("perk", args[2])));
                    return true;
                }
                perkManager.addPerk(target, perk);
                sender.sendMessage(plugin.getPrefix() + plugin.getMessage("perk-added", Map.of("player", target.getName(), "perk", perk.getKey())));
                return true;
            case "remove":
                if (!sender.hasPermission("boosterperks.admin")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("admin-no-permission", Map.of()));
                    return true;
                }
                if (args.length < 3) return true;
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-player", Map.of("player", args[1])));
                    return true;
                }
                perk = Perk.fromString(args[2]);
                if (perk == null) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-perk", Map.of("perk", args[2])));
                    return true;
                }
                perkManager.removePerk(target, perk);
                sender.sendMessage(plugin.getPrefix() + plugin.getMessage("perk-removed", Map.of("player", target.getName(), "perk", perk.getKey())));
                return true;
            case "reload":
                if (!sender.hasPermission("boosterperks.admin")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("admin-no-permission", Map.of()));
                    return true;
                }
                plugin.reload();
                sender.sendMessage(plugin.getPrefix() + plugin.getMessage("reload-success", Map.of()));
                return true;
            case "admin":
                if (!sender.hasPermission("boosterperks.admin")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessage("admin-no-permission", Map.of()));
                    return true;
                }
                adminManager.openMainAdminMenu(player);
                return true;
            default:
                menuManager.openMainMenu(player);
                return true;
        }
    }
}
