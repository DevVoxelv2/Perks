package com.example.perks.model;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public enum Perk {
    TELEKINESIS("telekinesis", "boosterperks.perks.telekinesis", PerkCategory.UTILITY, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==") {
        @Override
        public void apply(Player p) {
            // handled in listeners
        }

        @Override
        public void remove(Player p) {
        }
    },
    GLOW("glow", "boosterperks.perks.glow", PerkCategory.UTILITY, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjgwNTdhODcyNjFkYzY0OTE0ZmYwYTQ1ZThlYjRhZjFhOWVmMjI0NzNiZmEyZTkwYWE4MGM3YzA4MmYzZmMwNiJ9fX0=") {
        @Override
        public void apply(Player p) {
            p.setGlowing(true);
        }

        @Override
        public void remove(Player p) {
            p.setGlowing(false);
        }
    },
    NO_HUNGER("no-hunger", "boosterperks.perks.no-hunger", PerkCategory.SURVIVAL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTU5MWI2MTUyOWQyNWE3ZWNkNmJlYzAwOTQ4ZTZmZTE1NWUzMDA3ZjJkN2ZlNTU5ZjNhODNjNmY4MDhlNDM0ZCJ9fX0=") {
        @Override
        public void apply(Player p) {
        }

        @Override
        public void remove(Player p) {
        }
    },
    LAVA_RESISTANCE("lava-resistance", "boosterperks.perks.lavaresistance", PerkCategory.SURVIVAL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzIxZDA5MzBiZDYxZmVhNGNiOTAyN2IwMGU5NGUxM2Q2MjAyOWM1MjRlYTBiMzI2MGM3NDc0NTdiYTFiY2ZhMSJ9fX0=") {
        @Override
        public void apply(Player p) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
        }

        @Override
        public void remove(Player p) {
            p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
    },
    NO_FALL("no-fall", "boosterperks.perks.no-fall", PerkCategory.SURVIVAL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhkZDBlYjVjMDEwNzhmMTg0MzM0NTY1ZjEzMjQyMTE5YWI1MTQ3Nzc3NDcyMWQ2ZTUzYzcxYzM5ODYxM2E1NiJ9fX0=") {
        @Override
        public void apply(Player p) {
        }

        @Override
        public void remove(Player p) {
        }
    },
    WATER_BREATH("water-breath", "boosterperks.perks.waterbreath", PerkCategory.SURVIVAL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0ZGE0YTMxMGI4ZmRlMmJhMzY3ZTg5ZmUwOTkzNjIwNjg3MTBkYzdkNGFiNjJlOTVhZjA4OWJiYWI1YTc3In19fQ==") {
        @Override
        public void apply(Player p) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }

        @Override
        public void remove(Player p) {
            p.removePotionEffect(PotionEffectType.WATER_BREATHING);
        }
    },
    KEEP_XP("keep-xp", "boosterperks.perks.keepxp", PerkCategory.SURVIVAL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdkODg1YjMyYjBkZDJkNmI3ZjFiNTgyYTM0MTg2ZjhhNTM3M2M0NjU4OWEyNzM0MjMxMzJiNDQ4YjgwMzQ2MiJ9fX0=") {
        @Override
        public void apply(Player p) {
        }

        @Override
        public void remove(Player p) {
        }
    },
    KEEP_INVENTORY("keep-inventory", "boosterperks.perks.keepinventory", PerkCategory.SURVIVAL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE1MDIwY2VjYjAzODg0NzQ2ZWNlNzE2N2E2YWVlOWNiOGM3Y2U1ZDNkYzlkZWNiYzM2OTBiMjIyYzZlMjEyZSJ9fX0=") {
        @Override
        public void apply(Player p) {
        }

        @Override
        public void remove(Player p) {
        }
    },
    SPEED("speed", "boosterperks.perks.speed", PerkCategory.MOVEMENT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFhZTg3N2E4MjMwN2E1MmZhNDZhZGUzMzMzZTgxYzc5NjNjZTVkODliZmFhYmM2NzkzNGE2MTg2Y2FhOTUifX19") {
        @Override
        public void apply(Player p) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
        }

        @Override
        public void remove(Player p) {
            p.removePotionEffect(PotionEffectType.SPEED);
        }
    },
    NIGHT_VISION("night-vision", "boosterperks.perks.nightvision", PerkCategory.UTILITY, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGViNmU0ZDBmOTczNTRhMzk5Njg1MDFmNTgyZTdjZTc4YzcwZDNhMzMzOWE5ZmJmZjAxMGZhMDQ4YjRkNWJhMSJ9fX0=") {
        @Override
        public void apply(Player p) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }

        @Override
        public void remove(Player p) {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    },
    JUMP("jump", "boosterperks.perks.jump", PerkCategory.MOVEMENT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU1MjE1NzUxNjhlOWViNzg5ZDY1OTVkNTJjMzY4YTQ4NTQxZTcyOWI1NWQxMGI3MDRlNDRmYmYyODBkM2I3OSJ9fX0=") {
        @Override
        public void apply(Player p) {
            // Use compatible potion effect type
            PotionEffectType jumpEffect = getCompatiblePotionEffect("JUMP_BOOST", "JUMP");
            if (jumpEffect != null) {
                p.addPotionEffect(new PotionEffect(jumpEffect, Integer.MAX_VALUE, 0, true, false));
            }
        }

        @Override
        public void remove(Player p) {
            PotionEffectType jumpEffect = getCompatiblePotionEffect("JUMP_BOOST", "JUMP");
            if (jumpEffect != null) {
                p.removePotionEffect(jumpEffect);
            }
        }
    },
    HASTE("haste", "boosterperks.perks.haste", PerkCategory.MINING, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ5ODY3ZGRhNDdmMjkxODRmMThlYjI2MTc1MGMzYmE0ZDc2MzM4NzgzZDRmNjY5MWJlNTFmY2FlZDFiNTU3In19fQ==") {
        @Override
        public void apply(Player p) {
            // Use compatible potion effect type
            PotionEffectType hasteEffect = getCompatiblePotionEffect("HASTE", "FAST_DIGGING");
            if (hasteEffect != null) {
                p.addPotionEffect(new PotionEffect(hasteEffect, Integer.MAX_VALUE, 0, true, false));
            }
        }

        @Override
        public void remove(Player p) {
            PotionEffectType hasteEffect = getCompatiblePotionEffect("HASTE", "FAST_DIGGING");
            if (hasteEffect != null) {
                p.removePotionEffect(hasteEffect);
            }
        }
    },
    FLY("fly", "boosterperks.perks.fly", PerkCategory.MOVEMENT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FiMTEwNGExZWYzNGI2YjZmOGY5N2I0ZTk1OWFjODBlYWQyNDU1OWFlNWY3MWMzMmQ5MjFkMzQ3ZTJkMjRhZSJ9fX0=") {
        @Override
        public void apply(Player p) {
            p.setAllowFlight(true);
        }

        @Override
        public void remove(Player p) {
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    },
    INSTANT_SMELT("instant-smelt", "boosterperks.perks.instantsmelt", PerkCategory.MINING, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODYzYmQ5Y2JjYWMzOGIzYWQ0MzVkMTY5ODVmZGE4YzVkMjQ4ZWM5ZWE0NzkxNTE2M2NmYTc4YTM4MTAzNmNhOCJ9fX0=") {
        @Override
        public void apply(Player p) {
        }

        @Override
        public void remove(Player p) {
        }
    };

    private final String key;
    private final String permission;
    private final PerkCategory category;
    private final String texture;
    
    Perk(String key, String permission, PerkCategory category, String texture) {
        this.key = key;
        this.permission = permission;
        this.category = category;
        this.texture = texture;
    }

    public abstract void apply(Player p);
    public abstract void remove(Player p);

    public String getKey() {
        return key;
    }

    public String getPermission() {
        return permission;
    }
    
    public PerkCategory getCategory() {
        return category;
    }

    public ItemStack createIcon() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        // The GameProfile constructor no longer accepts a null name in recent
        // versions of the authentication library. Using a null value here caused
        // a NullPointerException when the perks menu attempted to create the
        // custom skull icon for a perk. To avoid this we provide a non-null
        // profile name; the actual value is irrelevant for our custom texture, so
        // we simply reuse the perk's key as the profile's name.
        GameProfile profile = new GameProfile(UUID.randomUUID(), key);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (Exception ignored) {
        }
        meta.setDisplayName(ChatColor.AQUA + key);
        head.setItemMeta(meta);
        return head;
    }

    public static Perk fromString(String s) {
        for (Perk perk : values()) {
            if (perk.key.equalsIgnoreCase(s)) {
                return perk;
            }
        }
        return null;
    }
    
    /**
     * Helper method to get compatible potion effect types
     */
    private static PotionEffectType getCompatiblePotionEffect(String newName, String oldName) {
        try {
            return PotionEffectType.getByName(newName);
        } catch (Exception e1) {
            try {
                return PotionEffectType.getByName(oldName);
            } catch (Exception e2) {
                // Try reflection for older versions
                try {
                    java.lang.reflect.Field field = PotionEffectType.class.getDeclaredField(newName);
                    return (PotionEffectType) field.get(null);
                } catch (Exception e3) {
                    try {
                        java.lang.reflect.Field field = PotionEffectType.class.getDeclaredField(oldName);
                        return (PotionEffectType) field.get(null);
                    } catch (Exception e4) {
                        return null;
                    }
                }
            }
        }
    }
}
