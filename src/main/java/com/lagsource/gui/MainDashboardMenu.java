package com.lagsource.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class MainDashboardMenu implements MenuView {
    private static final String TITLE = ChatColor.DARK_GREEN + "LagSource Dashboard";
    private final Inventory inventory;

    public MainDashboardMenu() {
        inventory = Bukkit.createInventory(null, 27, TITLE);
        populatePlaceholders();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(Player player, int slot, ItemStack item) {
        if (item == null) {
            return;
        }
        if (slot == 22) {
            player.sendMessage(ChatColor.YELLOW + "Refresh will be available in a later update.");
        }
        if (slot == 23) {
            player.sendMessage(ChatColor.YELLOW + "Settings view will be available in a later update.");
        }
        if (slot == 24) {
            player.sendMessage(ChatColor.YELLOW + "Help will be available in a later update.");
        }
    }

    private void populatePlaceholders() {
        inventory.setItem(10, item(Material.CLOCK, ChatColor.YELLOW + "TPS", List.of(ChatColor.GRAY + "Loading...")));
        inventory.setItem(12, item(Material.BARRIER, ChatColor.YELLOW + "Total Entities", List.of(ChatColor.GRAY + "Loading...")));
        inventory.setItem(14, item(Material.GRASS_BLOCK, ChatColor.YELLOW + "Worlds", List.of(ChatColor.GRAY + "Loading...")));
        inventory.setItem(16, item(Material.ZOMBIE_SPAWN_EGG, ChatColor.YELLOW + "Top Entities", List.of(ChatColor.GRAY + "Loading...")));
        inventory.setItem(18, item(Material.CHEST, ChatColor.YELLOW + "Top Chunks", List.of(ChatColor.GRAY + "Loading...")));

        inventory.setItem(22, item(Material.LIME_DYE, ChatColor.GREEN + "Refresh", List.of(ChatColor.GRAY + "Re-run snapshot")));
        inventory.setItem(23, item(Material.COMPARATOR, ChatColor.GOLD + "Settings", List.of(ChatColor.GRAY + "View config")));
        inventory.setItem(24, item(Material.BOOK, ChatColor.AQUA + "Help", List.of(ChatColor.GRAY + "Usage tips")));
    }

    private ItemStack item(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
