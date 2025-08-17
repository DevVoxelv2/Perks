package com.example.perks.commands;

import com.example.perks.managers.PerkManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkShopCommand implements CommandExecutor {
    private final PerkManager manager;

    public PerkShopCommand(PerkManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            manager.openShop(player);
        }
        return true;
    }
}
