package com.example.perks.listeners;

import com.example.perks.managers.PerkManager;
import com.example.perks.model.Perk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class PerkEffectListener implements Listener {
    private final PerkManager manager;

    public PerkEffectListener(PerkManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player && manager.isActive(player, Perk.NO_HUNGER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && event.getCause() == EntityDamageEvent.DamageCause.FALL && manager.isActive(player, Perk.NO_FALL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (manager.isActive(player, Perk.KEEP_INVENTORY)) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
        if (manager.isActive(player, Perk.KEEP_XP)) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        boolean tele = manager.isActive(player, Perk.TELEKINESIS);
        boolean smelt = manager.isActive(player, Perk.INSTANT_SMELT);
        if (tele || smelt) {
            Material type = event.getBlock().getType();
            ItemStack drop = null;
            if (smelt) {
                switch (type) {
                    case IRON_ORE, DEEPSLATE_IRON_ORE -> drop = new ItemStack(Material.IRON_INGOT);
                    case GOLD_ORE, DEEPSLATE_GOLD_ORE -> drop = new ItemStack(Material.GOLD_INGOT);
                    default -> {}
                }
            }
            event.setDropItems(false);
            if (drop == null) {
                for (ItemStack natural : event.getBlock().getDrops(player.getInventory().getItemInMainHand())) {
                    player.getInventory().addItem(natural);
                }
            } else {
                player.getInventory().addItem(drop);
            }
        }
    }
}
