package com.lagsource.gui;

import com.lagsource.snapshot.ChunkSnapshot;
import com.lagsource.snapshot.EntitySnapshot;
import com.lagsource.tps.TpsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;

public final class MainDashboardMenu implements MenuView {
    private static final String TITLE = ChatColor.DARK_GREEN + "LagSource Dashboard";
    private final Inventory inventory;

    public MainDashboardMenu(TpsProvider tpsProvider,
                             EntitySnapshot snapshot,
                             List<ChunkSnapshot> topChunks,
                             List<World> worlds,
                             List<Map.Entry<EntityType, Integer>> topEntities) {
        inventory = Bukkit.createInventory(null, 27, TITLE);
        populate(tpsProvider, snapshot, topChunks, worlds, topEntities);
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

    private void populate(TpsProvider tpsProvider,
                          EntitySnapshot snapshot,
                          List<ChunkSnapshot> topChunks,
                          List<World> worlds,
                          List<Map.Entry<EntityType, Integer>> topEntities) {
        inventory.setItem(10, tpsItem(tpsProvider));
        inventory.setItem(12, item(Material.BARRIER, ChatColor.YELLOW + "Total Entities",
                List.of(ChatColor.GRAY + formatCount(snapshot.getTotal()))));
        inventory.setItem(14, worldItem(worlds));
        inventory.setItem(16, topEntitiesItem(topEntities));
        inventory.setItem(18, topChunksItem(topChunks));

        inventory.setItem(22, item(Material.LIME_DYE, ChatColor.GREEN + "Refresh", List.of(ChatColor.GRAY + "Re-run snapshot")));
        inventory.setItem(23, item(Material.COMPARATOR, ChatColor.GOLD + "Settings", List.of(ChatColor.GRAY + "View config")));
        inventory.setItem(24, item(Material.BOOK, ChatColor.AQUA + "Help", List.of(ChatColor.GRAY + "Usage tips")));
    }

    private ItemStack tpsItem(TpsProvider tpsProvider) {
        OptionalDouble tps = tpsProvider.getTps();
        if (tps.isEmpty()) {
            return item(Material.CLOCK, ChatColor.YELLOW + "TPS", List.of(ChatColor.GRAY + "Unavailable"));
        }
        double value = tps.getAsDouble();
        ChatColor color = value >= 19.5 ? ChatColor.GREEN : value >= 17.5 ? ChatColor.YELLOW : ChatColor.RED;
        return item(Material.CLOCK, ChatColor.YELLOW + "TPS", List.of(color + String.format(Locale.US, "%.2f", value)));
    }

    private ItemStack worldItem(List<World> worlds) {
        List<String> lore = new ArrayList<>();
        for (World world : worlds) {
            lore.add(ChatColor.GRAY + world.getName());
        }
        if (lore.isEmpty()) {
            lore.add(ChatColor.GRAY + "No worlds loaded");
        }
        return item(Material.GRASS_BLOCK, ChatColor.YELLOW + "Worlds", lore);
    }

    private ItemStack topEntitiesItem(List<Map.Entry<EntityType, Integer>> topEntities) {
        List<String> lore = new ArrayList<>();
        for (Map.Entry<EntityType, Integer> entry : topEntities) {
            lore.add(ChatColor.GRAY + formatEntityType(entry.getKey()) + ": " + formatCount(entry.getValue()));
        }
        if (lore.isEmpty()) {
            lore.add(ChatColor.GRAY + "No entities");
        }
        return item(Material.ZOMBIE_SPAWN_EGG, ChatColor.YELLOW + "Top Entities", lore);
    }

    private ItemStack topChunksItem(List<ChunkSnapshot> topChunks) {
        List<String> lore = new ArrayList<>();
        for (ChunkSnapshot chunk : topChunks) {
            lore.add(ChatColor.GRAY + chunk.getWorldName() + " (" + chunk.getX() + ", " + chunk.getZ() + "): "
                    + formatCount(chunk.getEntityCount()));
        }
        if (lore.isEmpty()) {
            lore.add(ChatColor.GRAY + "No hot chunks");
        }
        return item(Material.CHEST, ChatColor.YELLOW + "Top Chunks", lore);
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

    private String formatCount(int count) {
        return NumberFormat.getIntegerInstance(Locale.US).format(count);
    }

    private String formatEntityType(EntityType type) {
        String[] parts = type.name().toLowerCase(Locale.US).split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }

    public static List<Map.Entry<EntityType, Integer>> sortTopEntities(EntitySnapshot snapshot, int max) {
        List<Map.Entry<EntityType, Integer>> entries = new ArrayList<>(snapshot.getCounts().entrySet());
        entries.sort(Comparator.comparingInt((Map.Entry<EntityType, Integer> entry) -> entry.getValue()).reversed());
        if (max > 0 && entries.size() > max) {
            return entries.subList(0, max);
        }
        return entries;
    }

    public static List<ChunkSnapshot> sortTopChunks(List<ChunkSnapshot> snapshots, int max) {
        snapshots.sort(Comparator.comparingInt(ChunkSnapshot::getEntityCount).reversed());
        if (max > 0 && snapshots.size() > max) {
            return snapshots.subList(0, max);
        }
        return snapshots;
    }
}
