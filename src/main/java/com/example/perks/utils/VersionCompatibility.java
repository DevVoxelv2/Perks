package com.example.perks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling version compatibility between Minecraft 1.20 - 1.21.8
 */
public class VersionCompatibility {
    
    private static final Logger LOGGER = Bukkit.getLogger();
    private static final String SERVER_VERSION = Bukkit.getVersion();
    private static final int[] VERSION_NUMBERS = parseVersion();
    
    // Version constants
    public static final boolean IS_1_20 = VERSION_NUMBERS[0] == 1 && VERSION_NUMBERS[1] == 20;
    public static final boolean IS_1_21_OR_NEWER = VERSION_NUMBERS[0] == 1 && VERSION_NUMBERS[1] >= 21;
    public static final boolean IS_SUPPORTED_VERSION = 
        (VERSION_NUMBERS[0] == 1 && VERSION_NUMBERS[1] >= 20 && VERSION_NUMBERS[1] <= 21) ||
        (VERSION_NUMBERS[0] == 1 && VERSION_NUMBERS[1] == 21 && VERSION_NUMBERS[2] <= 8);
    
    static {
        LOGGER.info("[NitroPerks] Detected server version: " + SERVER_VERSION);
        LOGGER.info("[NitroPerks] Parsed version: " + VERSION_NUMBERS[0] + "." + VERSION_NUMBERS[1] + "." + VERSION_NUMBERS[2]);
        LOGGER.info("[NitroPerks] Version supported: " + IS_SUPPORTED_VERSION);
        
        if (!IS_SUPPORTED_VERSION) {
            LOGGER.warning("[NitroPerks] This version may not be fully supported. Recommended: 1.20.x - 1.21.8");
        }
    }
    
    private static int[] parseVersion() {
        try {
            // Extract version from server version string
            String version = Bukkit.getBukkitVersion();
            String[] parts = version.split("-")[0].split("\\.");
            
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            
            return new int[]{major, minor, patch};
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[NitroPerks] Failed to parse server version, assuming 1.20.1", e);
            return new int[]{1, 20, 1};
        }
    }
    
    /**
     * Gets a compatible sound for the current version
     */
    public static Sound getCompatibleSound(String soundName) {
        try {
            return Sound.valueOf(soundName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fallback sounds for older versions
            return switch (soundName.toUpperCase()) {
                case "UI_BUTTON_CLICK" -> trySound("CLICK", "UI_BUTTON_CLICK");
                case "ENTITY_PLAYER_LEVELUP" -> trySound("LEVEL_UP", "ENTITY_PLAYER_LEVELUP");
                case "ENTITY_VILLAGER_YES" -> trySound("VILLAGER_YES", "ENTITY_VILLAGER_YES");
                case "ENTITY_VILLAGER_NO" -> trySound("VILLAGER_NO", "ENTITY_VILLAGER_NO");
                case "BLOCK_NOTE_BLOCK_BASS" -> trySound("NOTE_BASS", "BLOCK_NOTE_BLOCK_BASS");
                default -> Sound.BLOCK_NOTE_BLOCK_HARP; // Safe fallback
            };
        }
    }
    
    private static Sound trySound(String oldName, String newName) {
        try {
            return Sound.valueOf(newName);
        } catch (IllegalArgumentException e1) {
            try {
                return Sound.valueOf(oldName);
            } catch (IllegalArgumentException e2) {
                return Sound.BLOCK_NOTE_BLOCK_HARP;
            }
        }
    }
    
    /**
     * Gets a compatible particle for the current version
     */
    public static Particle getCompatibleParticle(String particleName) {
        try {
            return Particle.valueOf(particleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fallback particles for version compatibility
            return switch (particleName.toUpperCase()) {
                case "VILLAGER_HAPPY" -> tryParticle("HAPPY_VILLAGER", "VILLAGER_HAPPY");
                case "SMOKE_NORMAL" -> tryParticle("SMOKE", "SMOKE_NORMAL");
                case "ENCHANTMENT_TABLE" -> tryParticle("ENCHANTMENT_TABLE", "ENCHANT");
                case "FIREWORKS_SPARK" -> tryParticle("FIREWORK", "FIREWORKS_SPARK");
                case "END_ROD" -> tryParticle("END_ROD", "SPELL_WITCH");
                case "CLOUD" -> tryParticle("CLOUD", "SPELL");
                case "CRIT" -> tryParticle("CRIT", "CRIT_MAGIC");
                default -> Particle.HEART; // Safe fallback
            };
        }
    }
    
    private static Particle tryParticle(String newName, String oldName) {
        try {
            return Particle.valueOf(newName);
        } catch (IllegalArgumentException e1) {
            try {
                return Particle.valueOf(oldName);
            } catch (IllegalArgumentException e2) {
                return Particle.HEART;
            }
        }
    }
    
    /**
     * Gets a compatible material for the current version
     */
    public static Material getCompatibleMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fallback materials for version compatibility
            return switch (materialName.toUpperCase()) {
                case "PLAYER_HEAD" -> tryMaterial("PLAYER_HEAD", "SKULL_ITEM");
                case "BLACK_STAINED_GLASS_PANE" -> tryMaterial("BLACK_STAINED_GLASS_PANE", "STAINED_GLASS_PANE");
                case "NETHERITE_SWORD" -> tryMaterial("NETHERITE_SWORD", "DIAMOND_SWORD");
                case "WRITABLE_BOOK" -> tryMaterial("WRITABLE_BOOK", "BOOK_AND_QUILL");
                case "COMMAND_BLOCK" -> tryMaterial("COMMAND_BLOCK", "COMMAND");
                default -> Material.STONE; // Safe fallback
            };
        }
    }
    
    private static Material tryMaterial(String newName, String oldName) {
        try {
            return Material.valueOf(newName);
        } catch (IllegalArgumentException e1) {
            try {
                return Material.valueOf(oldName);
            } catch (IllegalArgumentException e2) {
                return Material.STONE;
            }
        }
    }
    
    /**
     * Sends a title to a player in a version-compatible way
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        } catch (Exception e) {
            // Fallback to chat message if title API is not available
            player.sendMessage(title);
            if (subtitle != null && !subtitle.isEmpty()) {
                player.sendMessage(subtitle);
            }
        }
    }
    
    /**
     * Sends an action bar message to a player in a version-compatible way
     */
    public static void sendActionBar(Player player, String message) {
        try {
            player.sendActionBar(message);
        } catch (Exception e) {
            // Fallback to chat message if action bar is not available
            player.sendMessage("§7[ActionBar] " + message);
        }
    }
    
    /**
     * Checks if the current server version supports a specific feature
     */
    public static boolean supportsFeature(String feature) {
        return switch (feature.toLowerCase()) {
            case "actionbar" -> IS_1_20 || IS_1_21_OR_NEWER;
            case "titles" -> IS_1_20 || IS_1_21_OR_NEWER;
            case "particles" -> IS_1_20 || IS_1_21_OR_NEWER;
            case "sounds" -> IS_1_20 || IS_1_21_OR_NEWER;
            case "customskulls" -> IS_1_20 || IS_1_21_OR_NEWER;
            case "folia" -> IS_1_21_OR_NEWER;
            default -> false;
        };
    }
    
    /**
     * Gets version-specific configuration values
     */
    public static String getVersionSpecificConfig(String key) {
        return switch (key.toLowerCase()) {
            case "database.driver" -> "com.mysql.cj.jdbc.Driver";
            case "database.url_params" -> "?useSSL=false&autoReconnect=true&serverTimezone=UTC";
            case "gui.title_length" -> IS_1_21_OR_NEWER ? "64" : "32";
            default -> "";
        };
    }
    
    /**
     * Logs version compatibility information
     */
    public static void logCompatibilityInfo() {
        LOGGER.info("=== NitroPerks Version Compatibility ===");
        LOGGER.info("Server Version: " + SERVER_VERSION);
        LOGGER.info("Bukkit Version: " + Bukkit.getBukkitVersion());
        LOGGER.info("API Version: " + Bukkit.getServer().getClass().getPackage().getName());
        LOGGER.info("Supported Version: " + IS_SUPPORTED_VERSION);
        LOGGER.info("ActionBar Support: " + supportsFeature("actionbar"));
        LOGGER.info("Title Support: " + supportsFeature("titles"));
        LOGGER.info("Particle Support: " + supportsFeature("particles"));
        LOGGER.info("Custom Skulls Support: " + supportsFeature("customskulls"));
        LOGGER.info("Folia Support: " + supportsFeature("folia"));
        LOGGER.info("========================================");
    }
}
