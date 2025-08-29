package com.example.perks.commands;

import com.example.perks.managers.MenuManager;
import com.example.perks.managers.PerkManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkShopCommand implements CommandExecutor {
    private final PerkManager perkManager;
    private final MenuManager menuManager;

    public PerkShopCommand(PerkManager perkManager, MenuManager menuManager) {
        this.perkManager = perkManager;
        this.menuManager = menuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            menuManager.openMainMenu(player); // Opens main menu, user can navigate to shop from there
        }
        return true;
    }
}
