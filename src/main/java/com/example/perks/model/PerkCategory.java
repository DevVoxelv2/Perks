package com.example.perks.model;

import org.bukkit.Material;

public enum PerkCategory {
    COMBAT("&c&lCombat Perks", Material.NETHERITE_SWORD, "Combat-focused abilities"),
    UTILITY("&a&lUtility Perks", Material.ENDER_CHEST, "Useful everyday abilities"),
    MOVEMENT("&b&lMovement Perks", Material.FEATHER, "Movement enhancement abilities"),
    SURVIVAL("&e&lSurvival Perks", Material.GOLDEN_APPLE, "Survival assistance abilities"),
    MINING("&6&lMining Perks", Material.DIAMOND_PICKAXE, "Mining enhancement abilities");
    
    private final String displayName;
    private final Material icon;
    private final String description;
    
    PerkCategory(String displayName, Material icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Material getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
}
