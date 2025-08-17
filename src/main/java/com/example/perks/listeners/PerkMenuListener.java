package com.example.perks.listeners;

import com.example.perks.managers.PerkManager;
import com.example.perks.model.Perk;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PerkMenuListener implements Listener {
    private final PerkManager manager;

    public PerkMenuListener(PerkManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = ChatColor.stripColor(event.getView().getTitle()).toLowerCase();
        if (!(title.contains("perk"))) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (event.getCurrentItem() == null) return;
        int slot = event.getRawSlot();
        Perk[] perks = Perk.values();
        if (slot >= perks.length) return;
        Perk perk = perks[slot];
        if (title.contains("shop")) {
            manager.purchase(player, perk);
        } else {
            manager.toggle(player, perk);
        }
    }
}
