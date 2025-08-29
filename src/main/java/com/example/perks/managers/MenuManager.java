package com.example.perks.managers;

import com.example.perks.PerksPlugin;
import com.example.perks.model.Perk;
import com.example.perks.model.PerkCategory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Sound;

import java.io.File;
import java.text.NumberFormat;
import java.util.*;

public class MenuManager {
    private final PerksPlugin plugin;
    private final PerkManager perkManager;
    private FileConfiguration menuConfig;
    private final Map<String, Inventory> cachedMenus = new HashMap<>();
    
    public MenuManager(PerksPlugin plugin, PerkManager perkManager) {
        this.plugin = plugin;
        this.perkManager = perkManager;
        loadMenuConfig();
    }
    
    public void loadMenuConfig() {
        File menuFile = new File(plugin.getDataFolder(), "menus.yml");
        if (!menuFile.exists()) {
            plugin.saveResource("menus.yml", false);
        }
        menuConfig = YamlConfiguration.loadConfiguration(menuFile);
        cachedMenus.clear(); // Clear cache when reloading
    }
    
    public void openMainMenu(Player player) {
        String menuKey = "main-menu";
        Inventory inv = createMenuFromConfig(menuKey, player);
        
        // Add category items
        int slot = 10;
        for (PerkCategory category : PerkCategory.values()) {
            ItemStack categoryItem = createCategoryItem(category, player);
            inv.setItem(slot, categoryItem);
            slot += 2; // Space items out
        }
        
        // Add shop access
        ItemStack shopItem = createShopAccessItem();
        inv.setItem(26, shopItem);
        
        // Add close button
        ItemStack closeItem = createCloseItem();
        inv.setItem(22, closeItem);
        
        player.openInventory(inv);
        playSound(player, Sound.UI_BUTTON_CLICK);
    }
    
    public void openPerkOverview(Player player, PerkCategory category) {
        String menuKey = "perks-overview.page1";
        Inventory inv = createMenuFromConfig(menuKey, player);
        
        List<Perk> categoryPerks = Arrays.stream(Perk.values())
            .filter(perk -> perk.getCategory() == category)
            .toList();
        
        int slot = 10;
        for (Perk perk : categoryPerks) {
            ItemStack perkItem = createPerkOverviewItem(perk, player);
            inv.setItem(slot, perkItem);
            slot++;
            if (slot == 17) slot = 19; // Skip to next row
            if (slot == 26) slot = 28; // Skip to next row
        }
        
        // Add back button
        ItemStack backItem = createBackItem();
        inv.setItem(31, backItem);
        
        player.openInventory(inv);
        playSound(player, Sound.UI_BUTTON_CLICK);
    }
    
    public void openShopMenu(Player player, PerkCategory category) {
        String menuKey = "shop-menu.page1";
        Inventory inv = createMenuFromConfig(menuKey, player);
        
        List<Perk> categoryPerks = Arrays.stream(Perk.values())
            .filter(perk -> perk.getCategory() == category)
            .toList();
        
        int slot = 10;
        for (Perk perk : categoryPerks) {
            ItemStack perkItem = createShopPerkItem(perk, player);
            inv.setItem(slot, perkItem);
            slot++;
            if (slot == 17) slot = 19; // Skip to next row
            if (slot == 26) slot = 28; // Skip to next row
        }
        
        // Add back button
        ItemStack backItem = createBackItem();
        inv.setItem(31, backItem);
        
        player.openInventory(inv);
        playSound(player, Sound.UI_BUTTON_CLICK);
    }
    
    private Inventory createMenuFromConfig(String menuPath, Player player) {
        ConfigurationSection section = menuConfig.getConfigurationSection(menuPath);
        if (section == null) {
            return Bukkit.createInventory(null, 27, "§cMenu Configuration Error");
        }
        
        int rows = section.getInt("rows", 3);
        String title = ChatColor.translateAlternateColorCodes('&', section.getString("title", "Menu"));
        
        return Bukkit.createInventory(null, rows * 9, title);
    }
    
    private ItemStack createCategoryItem(PerkCategory category, Player player) {
        ItemStack item = new ItemStack(category.getIcon());
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', category.getDisplayName()));
        
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Category: §f" + category.getDisplayName());
        
        long ownedPerks = Arrays.stream(Perk.values())
            .filter(perk -> perk.getCategory() == category)
            .filter(perk -> perkManager.hasUnlocked(player, perk))
            .count();
        
        long totalPerks = Arrays.stream(Perk.values())
            .filter(perk -> perk.getCategory() == category)
            .count();
        
        lore.add("§7Owned: §a" + ownedPerks + "§7/§f" + totalPerks);
        lore.add("");
        lore.add("§eClick to view perks!");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack createPerkOverviewItem(Perk perk, Player player) {
        ItemStack item = perk.createIcon();
        ItemMeta meta = item.getItemMeta();
        
        String displayName = "§b" + formatPerkName(perk.getKey());
        meta.setDisplayName(displayName);
        
        List<String> lore = new ArrayList<>();
        lore.add("");
        
        if (!perkManager.hasUnlocked(player, perk)) {
            lore.add("§c✗ Not Owned");
            lore.add("§7Purchase in the shop first!");
        } else if (perkManager.isActive(player, perk)) {
            lore.add("§a✓ Active");
            lore.add("§7This perk is currently enabled.");
            lore.add("");
            lore.add("§eClick to disable!");
        } else {
            lore.add("§6⚡ Owned");
            lore.add("§7This perk is currently disabled.");
            lore.add("");
            lore.add("§eClick to enable!");
        }
        
        lore.add("");
        lore.add("§7Description:");
        lore.addAll(getPerkDescription(perk));
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack createShopPerkItem(Perk perk, Player player) {
        ItemStack item = perk.createIcon();
        ItemMeta meta = item.getItemMeta();
        
        String displayName = "§b" + formatPerkName(perk.getKey());
        meta.setDisplayName(displayName);
        
        List<String> lore = new ArrayList<>();
        lore.add("");
        
        double price = plugin.getPrice(perk);
        String formattedPrice = NumberFormat.getInstance().format(price);
        
        if (perkManager.hasUnlocked(player, perk)) {
            lore.add("§a✓ Already Owned");
            lore.add("§7You already own this perk.");
        } else {
            double balance = plugin.getEconomy().getBalance(player);
            if (balance >= price) {
                lore.add("§a$ " + formattedPrice);
                lore.add("§7You can afford this perk!");
                lore.add("");
                lore.add("§eClick to purchase!");
            } else {
                lore.add("§c$ " + formattedPrice);
                lore.add("§7You need §c$" + NumberFormat.getInstance().format(price - balance) + " §7more!");
            }
        }
        
        lore.add("");
        lore.add("§7Description:");
        lore.addAll(getPerkDescription(perk));
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack createShopAccessItem() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName("§a§lPerk Shop");
        List<String> lore = Arrays.asList(
            "",
            "§7Purchase new perks with money!",
            "",
            "§eClick to open shop!"
        );
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName("§c« Back");
        List<String> lore = Arrays.asList(
            "",
            "§7Return to the main menu.",
            "",
            "§eClick to go back!"
        );
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack createCloseItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName("§c§lClose Menu");
        List<String> lore = Arrays.asList(
            "",
            "§7Close the perks menu.",
            "",
            "§eClick to close!"
        );
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private List<String> getPerkDescription(Perk perk) {
        List<String> description = new ArrayList<>();
        
        switch (perk) {
            case TELEKINESIS -> {
                description.add("§7Automatically collect dropped");
                description.add("§7items into your inventory.");
            }
            case GLOW -> {
                description.add("§7Makes you glow with a");
                description.add("§7visible outline effect.");
            }
            case NO_HUNGER -> {
                description.add("§7Prevents hunger depletion");
                description.add("§7so you never get hungry.");
            }
            case LAVA_RESISTANCE -> {
                description.add("§7Grants immunity to lava");
                description.add("§7and fire damage.");
            }
            case NO_FALL -> {
                description.add("§7Prevents fall damage");
                description.add("§7from any height.");
            }
            case WATER_BREATH -> {
                description.add("§7Allows unlimited breathing");
                description.add("§7underwater without drowning.");
            }
            case KEEP_XP -> {
                description.add("§7Keep your experience points");
                description.add("§7when you die.");
            }
            case KEEP_INVENTORY -> {
                description.add("§7Keep your items in your");
                description.add("§7inventory when you die.");
            }
            case SPEED -> {
                description.add("§7Increases your movement");
                description.add("§7speed permanently.");
            }
            case NIGHT_VISION -> {
                description.add("§7See clearly in the dark");
                description.add("§7as if it were daytime.");
            }
            case JUMP -> {
                description.add("§7Jump higher than normal");
                description.add("§7with increased jump boost.");
            }
            case HASTE -> {
                description.add("§7Mine blocks faster with");
                description.add("§7permanent haste effect.");
            }
            case FLY -> {
                description.add("§7Enables creative-like flight");
                description.add("§7in survival mode.");
            }
            case INSTANT_SMELT -> {
                description.add("§7Automatically smelts ores");
                description.add("§7when you mine them.");
            }
        }
        
        return description;
    }
    
    private String formatPerkName(String key) {
        return Arrays.stream(key.split("-"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
            .reduce((a, b) -> a + " " + b)
            .orElse(key);
    }
    
    private void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 0.5f, 1.0f);
    }
    
    public String getMenuType(String title) {
        String stripped = ChatColor.stripColor(title).toLowerCase();
        if (stripped.contains("shop")) return "shop";
        if (stripped.contains("overview")) return "overview";
        if (stripped.contains("perks")) return "main";
        return "unknown";
    }
}
