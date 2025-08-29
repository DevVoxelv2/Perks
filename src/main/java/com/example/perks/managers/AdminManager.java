package com.example.perks.managers;

import com.example.perks.PerksPlugin;
import com.example.perks.model.Perk;
import com.example.perks.utils.VersionCompatibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminManager {
    
    private final PerksPlugin plugin;
    private final PerkManager perkManager;
    private final DatabaseManager databaseManager;
    
    public AdminManager(PerksPlugin plugin, PerkManager perkManager) {
        this.plugin = plugin;
        this.perkManager = perkManager;
        this.databaseManager = plugin.getDatabaseManager();
    }
    
    public void openMainAdminMenu(Player admin) {
        if (!admin.hasPermission("nitroperks.admin")) {
            admin.sendMessage(plugin.getPrefix() + "§cYou don't have permission to access the admin menu.");
            return;
        }
        
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lNitroPerks Admin Panel");
        
        // Player Management
        ItemStack playerManagement = new ItemStack(VersionCompatibility.getCompatibleMaterial("PLAYER_HEAD"));
        ItemMeta playerMeta = playerManagement.getItemMeta();
        playerMeta.setDisplayName("§a§lPlayer Management");
        playerMeta.setLore(Arrays.asList(
            "",
            "§7Manage individual player perks",
            "§7and view player statistics.",
            "",
            "§eClick to open!"
        ));
        playerManagement.setItemMeta(playerMeta);
        inv.setItem(10, playerManagement);
        
        // Perk Configuration
        ItemStack perkConfig = new ItemStack(VersionCompatibility.getCompatibleMaterial("WRITABLE_BOOK"));
        ItemMeta configMeta = perkConfig.getItemMeta();
        configMeta.setDisplayName("§b§lPerk Configuration");
        configMeta.setLore(Arrays.asList(
            "",
            "§7Configure perk prices,",
            "§7permissions and settings.",
            "",
            "§eClick to configure!"
        ));
        perkConfig.setItemMeta(configMeta);
        inv.setItem(12, perkConfig);
        
        // Statistics
        ItemStack stats = new ItemStack(Material.BOOK);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName("§6§lServer Statistics");
        statsMeta.setLore(Arrays.asList(
            "",
            "§7View server-wide perk",
            "§7usage and purchase statistics.",
            "",
            "§eClick to view!"
        ));
        stats.setItemMeta(statsMeta);
        inv.setItem(14, stats);
        
        // Reload Configuration
        ItemStack reload = new ItemStack(VersionCompatibility.getCompatibleMaterial("COMMAND_BLOCK"));
        ItemMeta reloadMeta = reload.getItemMeta();
        reloadMeta.setDisplayName("§e§lReload Configuration");
        reloadMeta.setLore(Arrays.asList(
            "",
            "§7Reload all configuration files",
            "§7including messages and menus.",
            "",
            "§eClick to reload!"
        ));
        reload.setItemMeta(reloadMeta);
        inv.setItem(16, reload);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§c§lClose Menu");
        closeMeta.setLore(Arrays.asList(
            "",
            "§7Close the admin panel.",
            "",
            "§eClick to close!"
        ));
        close.setItemMeta(closeMeta);
        inv.setItem(22, close);
        
        // Fill empty slots with glass panes
        ItemStack glass = new ItemStack(VersionCompatibility.getCompatibleMaterial("BLACK_STAINED_GLASS_PANE"));
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }
        
        admin.openInventory(inv);
    }
    
    public void openPlayerManagement(Player admin) {
        Inventory inv = Bukkit.createInventory(null, 54, "§4§lPlayer Management");
        
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        
        for (int i = 0; i < Math.min(onlinePlayers.size(), 45); i++) {
            Player target = onlinePlayers.get(i);
            ItemStack playerHead = new ItemStack(VersionCompatibility.getCompatibleMaterial("PLAYER_HEAD"));
            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            
            meta.setOwningPlayer(target);
            meta.setDisplayName("§a" + target.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7UUID: §f" + target.getUniqueId().toString());
            
            int ownedPerks = databaseManager.getUnlockedPerks(target.getUniqueId()).size();
            int activePerks = databaseManager.getActivePerks(target.getUniqueId()).size();
            
            lore.add("§7Owned Perks: §a" + ownedPerks + "§7/§f" + Perk.values().length);
            lore.add("§7Active Perks: §b" + activePerks);
            lore.add("");
            lore.add("§eClick to manage this player!");
            
            meta.setLore(lore);
            playerHead.setItemMeta(meta);
            
            inv.setItem(i, playerHead);
        }
        
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c« Back to Admin Panel");
        back.setItemMeta(backMeta);
        inv.setItem(49, back);
        
        admin.openInventory(inv);
    }
    
    public void openPlayerPerksMenu(Player admin, Player target) {
        Inventory inv = Bukkit.createInventory(null, 54, "§4§lManage " + target.getName());
        
        int slot = 0;
        for (Perk perk : Perk.values()) {
            if (slot >= 45) break; // Leave space for navigation
            
            ItemStack item = perk.createIcon();
            ItemMeta meta = item.getItemMeta();
            
            boolean hasUnlocked = perkManager.hasUnlocked(target, perk);
            boolean isActive = perkManager.isActive(target, perk);
            
            String displayName = "§b" + formatPerkName(perk.getKey());
            if (hasUnlocked) {
                if (isActive) {
                    displayName += " §a(Active)";
                } else {
                    displayName += " §6(Owned)";
                }
            } else {
                displayName += " §c(Not Owned)";
            }
            
            meta.setDisplayName(displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Status: " + (hasUnlocked ? (isActive ? "§aActive" : "§6Owned") : "§cNot Owned"));
            lore.add("§7Price: §e$" + NumberFormat.getInstance().format(plugin.getPrice(perk)));
            lore.add("");
            
            if (hasUnlocked) {
                lore.add("§aLeft Click: §7Toggle perk");
                lore.add("§cRight Click: §7Remove perk");
            } else {
                lore.add("§aLeft Click: §7Give perk");
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            
            inv.setItem(slot++, item);
        }
        
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c« Back to Player Management");
        back.setItemMeta(backMeta);
        inv.setItem(49, back);
        
        admin.openInventory(inv);
    }
    
    public void openPerkConfiguration(Player admin) {
        Inventory inv = Bukkit.createInventory(null, 54, "§4§lPerk Configuration");
        
        int slot = 0;
        for (Perk perk : Perk.values()) {
            if (slot >= 45) break;
            
            ItemStack item = perk.createIcon();
            ItemMeta meta = item.getItemMeta();
            
            meta.setDisplayName("§b" + formatPerkName(perk.getKey()));
            
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Current Price: §e$" + NumberFormat.getInstance().format(plugin.getPrice(perk)));
            lore.add("§7Permission: §f" + perk.getPermission());
            lore.add("§7Category: §a" + perk.getCategory().getDisplayName());
            lore.add("");
            lore.add("§eClick to modify price!");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            
            inv.setItem(slot++, item);
        }
        
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c« Back to Admin Panel");
        back.setItemMeta(backMeta);
        inv.setItem(49, back);
        
        admin.openInventory(inv);
    }
    
    public void givePerkToPlayer(Player target, Perk perk) {
        perkManager.addPerk(target, perk);
    }
    
    public void removePerkFromPlayer(Player target, Perk perk) {
        perkManager.removePerk(target, perk);
    }
    
    public void togglePerkForPlayer(Player target, Perk perk) {
        perkManager.toggle(target, perk);
    }
    
    private String formatPerkName(String key) {
        return Arrays.stream(key.split("-"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
            .reduce((a, b) -> a + " " + b)
            .orElse(key);
    }
    
    public String getMenuType(String title) {
        String stripped = ChatColor.stripColor(title).toLowerCase();
        if (stripped.contains("admin panel")) return "main";
        if (stripped.contains("player management")) return "player_management";
        if (stripped.contains("manage ")) return "player_perks";
        if (stripped.contains("perk configuration")) return "perk_config";
        return "unknown";
    }
}
