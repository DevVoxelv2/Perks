package com.example.perks.managers;

import com.example.perks.PerksPlugin;
import com.example.perks.utils.VersionCompatibility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class EffectsManager {
    private final PerksPlugin plugin;
    private FileConfiguration messages;
    private boolean soundsEnabled;
    private boolean particlesEnabled;
    
    public EffectsManager(PerksPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void loadConfig() {
        messages = plugin.getMessages();
        soundsEnabled = messages.getBoolean("sounds.enabled", true);
        particlesEnabled = messages.getBoolean("particles.enabled", true);
    }
    
    public void playPerkActivateEffects(Player player) {
        if (soundsEnabled) {
            String soundName = messages.getString("sounds.perk-activate", "ENTITY_PLAYER_LEVELUP");
            playSound(player, soundName);
        }
        
        if (particlesEnabled) {
            String particleName = messages.getString("particles.perk-activate", "VILLAGER_HAPPY");
            spawnParticles(player, particleName, 10);
        }
        
        // Title effect
        VersionCompatibility.sendTitle(player, "§a§lPERK ACTIVATED", "§7Your perk is now active!", 10, 40, 10);
    }
    
    public void playPerkDeactivateEffects(Player player) {
        if (soundsEnabled) {
            String soundName = messages.getString("sounds.perk-deactivate", "BLOCK_NOTE_BLOCK_BASS");
            playSound(player, soundName);
        }
        
        if (particlesEnabled) {
            String particleName = messages.getString("particles.perk-deactivate", "SMOKE_NORMAL");
            spawnParticles(player, particleName, 5);
        }
        
        // Title effect
        VersionCompatibility.sendTitle(player, "§c§lPERK DEACTIVATED", "§7Your perk has been disabled.", 10, 40, 10);
    }
    
    public void playPurchaseSuccessEffects(Player player) {
        if (soundsEnabled) {
            String soundName = messages.getString("sounds.purchase-success", "ENTITY_VILLAGER_YES");
            playSound(player, soundName);
        }
        
        if (particlesEnabled) {
            spawnParticles(player, "VILLAGER_HAPPY", 15);
            
            // Delayed firework effect
            new BukkitRunnable() {
                @Override
                public void run() {
                    spawnParticles(player, "FIREWORKS_SPARK", 20);
                }
            }.runTaskLater(plugin, 10L);
        }
        
        // Title effect
        VersionCompatibility.sendTitle(player, "§a§l✓ PURCHASED", "§7Perk unlocked successfully!", 10, 60, 10);
        
        // Action bar
        VersionCompatibility.sendActionBar(player, "§a§l» §7New perk added to your collection! §a§l«");
    }
    
    public void playPurchaseFailEffects(Player player) {
        if (soundsEnabled) {
            String soundName = messages.getString("sounds.purchase-fail", "ENTITY_VILLAGER_NO");
            playSound(player, soundName);
        }
        
        if (particlesEnabled) {
            spawnParticles(player, "SMOKE_NORMAL", 8);
        }
        
        // Action bar
        VersionCompatibility.sendActionBar(player, "§c§l» §7Not enough money to purchase this perk! §c§l«");
    }
    
    public void playMenuClickEffect(Player player) {
        if (soundsEnabled) {
            String soundName = messages.getString("sounds.menu-click", "UI_BUTTON_CLICK");
            playSound(player, soundName, 0.5f, 1.2f);
        }
    }
    
    public void playPerkLevelUpEffect(Player player, int newLevel) {
        if (soundsEnabled) {
            playSound(player, "ENTITY_PLAYER_LEVELUP", 1.0f, 1.5f);
        }
        
        if (particlesEnabled) {
            // Create a spiral effect
            Location loc = player.getLocation().add(0, 1, 0);
            new BukkitRunnable() {
                double t = 0;
                int count = 0;
                
                @Override
                public void run() {
                    if (count >= 40) {
                        cancel();
                        return;
                    }
                    
                    double x = Math.cos(t) * 1.5;
                    double z = Math.sin(t) * 1.5;
                    double y = t * 0.1;
                    
                    Location particleLoc = loc.clone().add(x, y, z);
                    Particle enchantParticle = VersionCompatibility.getCompatibleParticle("ENCHANTMENT_TABLE");
                    player.getWorld().spawnParticle(enchantParticle, particleLoc, 1, 0, 0, 0, 0);
                    
                    t += 0.3;
                    count++;
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
        
        // Title effect
        VersionCompatibility.sendTitle(player, "§6§lLEVEL UP!", "§7Perk upgraded to level " + newLevel, 10, 60, 10);
    }
    
    private void playSound(Player player, String soundName) {
        playSound(player, soundName, 1.0f, 1.0f);
    }
    
    private void playSound(Player player, String soundName, float volume, float pitch) {
        try {
            Sound sound = VersionCompatibility.getCompatibleSound(soundName);
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to play sound: " + soundName, e);
        }
    }
    
    private void spawnParticles(Player player, String particleName, int count) {
        try {
            Particle particle = VersionCompatibility.getCompatibleParticle(particleName);
            Location loc = player.getLocation().add(0, 1, 0);
            player.getWorld().spawnParticle(particle, loc, count, 0.5, 0.5, 0.5, 0);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to spawn particles: " + particleName, e);
        }
    }
    
    public void createPerkAura(Player player, String perkKey) {
        if (!particlesEnabled) return;
        
        // Create different auras for different perk types
        Particle particle = switch (perkKey) {
            case "glow" -> VersionCompatibility.getCompatibleParticle("END_ROD");
            case "speed" -> VersionCompatibility.getCompatibleParticle("CLOUD");
            case "fly" -> VersionCompatibility.getCompatibleParticle("FIREWORKS_SPARK");
            case "night-vision" -> VersionCompatibility.getCompatibleParticle("ENCHANTMENT_TABLE");
            case "haste" -> VersionCompatibility.getCompatibleParticle("CRIT");
            default -> VersionCompatibility.getCompatibleParticle("VILLAGER_HAPPY");
        };
        
        // Spawn particles around the player
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(particle, loc, 1, 0.3, 0.3, 0.3, 0);
    }
    
    public void showPerkStatusChange(Player player, String perkName, boolean activated) {
        String status = activated ? "§aACTIVATED" : "§cDEACTIVATED";
        String color = activated ? "§a" : "§c";
        
        VersionCompatibility.sendActionBar(player, color + "§l» §f" + perkName + " " + status + " " + color + "§l«");
        
        if (activated) {
            playPerkActivateEffects(player);
        } else {
            playPerkDeactivateEffects(player);
        }
    }
}
