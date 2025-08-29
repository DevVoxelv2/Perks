package com.example.perks.listeners;

import com.example.perks.managers.EffectsManager;
import com.example.perks.managers.MenuManager;
import com.example.perks.managers.PerkManager;
import com.example.perks.model.Perk;
import com.example.perks.model.PerkCategory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PerkMenuListener implements Listener {
    private final PerkManager perkManager;
    private final MenuManager menuManager;
    private final EffectsManager effectsManager;

    public PerkMenuListener(PerkManager perkManager, MenuManager menuManager, EffectsManager effectsManager) {
        this.perkManager = perkManager;
        this.menuManager = menuManager;
        this.effectsManager = effectsManager;
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
        
        String menuType = menuManager.getMenuType(title);
        int slot = event.getRawSlot();
        
        switch (menuType) {
            case "main" -> handleMainMenuClick(player, slot);
            case "overview" -> handleOverviewClick(player, slot);
            case "shop" -> handleShopClick(player, slot);
            default -> {
                // Fallback to old system for compatibility
                Perk[] perks = Perk.values();
                if (slot >= perks.length) return;
                Perk perk = perks[slot];
                if (title.contains("shop")) {
                    perkManager.purchase(player, perk);
                } else {
                    perkManager.toggle(player, perk);
                }
            }
        }
    }
    
    private void handleMainMenuClick(Player player, int slot) {
        effectsManager.playMenuClickEffect(player);
        
        // Handle category selection
        PerkCategory[] categories = PerkCategory.values();
        int categoryIndex = getCategoryIndexFromSlot(slot);
        
        if (categoryIndex >= 0 && categoryIndex < categories.length) {
            PerkCategory category = categories[categoryIndex];
            menuManager.openPerkOverview(player, category);
        } else if (slot == 26) { // Shop access button
            menuManager.openShopMenu(player, PerkCategory.UTILITY); // Default to utility category
        } else if (slot == 22) { // Close button
            player.closeInventory();
        }
    }
    
    private void handleOverviewClick(Player player, int slot) {
        effectsManager.playMenuClickEffect(player);
        
        if (slot == 31) { // Back button
            menuManager.openMainMenu(player);
            return;
        }
        
        Perk perk = getPerkFromSlot(slot);
        if (perk != null) {
            perkManager.toggle(player, perk);
        }
    }
    
    private void handleShopClick(Player player, int slot) {
        effectsManager.playMenuClickEffect(player);
        
        if (slot == 31) { // Back button
            menuManager.openMainMenu(player);
            return;
        }
        
        Perk perk = getPerkFromSlot(slot);
        if (perk != null) {
            perkManager.purchase(player, perk);
        }
    }
    
    private int getCategoryIndexFromSlot(int slot) {
        // Map slots to category indices: 10, 12, 14, 16, 18
        return switch (slot) {
            case 10 -> 0;
            case 12 -> 1;
            case 14 -> 2;
            case 16 -> 3;
            case 18 -> 4;
            default -> -1;
        };
    }
    
    private Perk getPerkFromSlot(int slot) {
        // Convert inventory slot to perk index for display slots
        if (slot >= 10 && slot <= 16) {
            int index = slot - 10;
            Perk[] perks = Perk.values();
            return index < perks.length ? perks[index] : null;
        } else if (slot >= 19 && slot <= 25) {
            int index = slot - 19 + 7;
            Perk[] perks = Perk.values();
            return index < perks.length ? perks[index] : null;
        } else if (slot >= 28 && slot <= 34) {
            int index = slot - 28 + 14;
            Perk[] perks = Perk.values();
            return index < perks.length ? perks[index] : null;
        }
        return null;
    }
}
