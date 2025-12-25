package com.lagsource.command;

import com.lagsource.LagSourcePlugin;
import com.lagsource.snapshot.ChunkSnapshot;
import com.lagsource.snapshot.ChunkSnapshotService;
import com.lagsource.snapshot.EntitySnapshot;
import com.lagsource.snapshot.EntitySnapshotService;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LagSourceCommand implements CommandExecutor {
    private static final String PERMISSION_USE = "lagsource.use";
    private final LagSourcePlugin plugin;
    private final EntitySnapshotService entitySnapshotService = new EntitySnapshotService();
    private final ChunkSnapshotService chunkSnapshotService = new ChunkSnapshotService();

    public LagSourceCommand(LagSourcePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION_USE)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use LagSource.");
            return true;
        }

        boolean includePlayers = plugin.getConfig().getBoolean("include-players", false);
        int maxReport = Math.max(0, plugin.getConfig().getInt("max-entity-report", 5));
        int chunkWarning = Math.max(0, plugin.getConfig().getInt("chunk-entity-warning", 100));
        boolean chunkOnly = args.length > 0 && args[0].equalsIgnoreCase("chunk");
        List<World> worlds = plugin.getServer().getWorlds();
        boolean reportDisabled = maxReport == 0;

        sender.sendMessage(ChatColor.GOLD + (chunkOnly ? "LagSource Chunk Snapshot" : "LagSource Snapshot"));
        sender.sendMessage(ChatColor.GRAY + "LagSource is not a profiler.");
        if (worlds.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "No loaded worlds.");
            return true;
        }

        EntitySnapshot snapshot = null;
        List<Map.Entry<EntityType, Integer>> topEntities = List.of();
        if (!chunkOnly) {
            snapshot = entitySnapshotService.snapshot(worlds, includePlayers);
            topEntities = topEntities(snapshot.getCounts(), maxReport);
        }
        List<ChunkSnapshot> topChunks = topChunks(chunkSnapshotService.snapshot(worlds), maxReport);
        if (!chunkOnly) {
            sender.sendMessage(ChatColor.GREEN + "Total Entities: " + formatCount(snapshot.getTotal()));
            sender.sendMessage(ChatColor.YELLOW + "Top Entities:");
            if (reportDisabled) {
                sender.sendMessage(ChatColor.GRAY + "Entity reporting disabled (max-entity-report=0).");
            } else if (topEntities.isEmpty()) {
                sender.sendMessage(ChatColor.GRAY + "No loaded entities.");
            } else {
                for (Map.Entry<EntityType, Integer> entry : topEntities) {
                    sender.sendMessage(ChatColor.GRAY + formatEntityType(entry.getKey()) + ": " + formatCount(entry.getValue()));
                }
            }
        }

        sender.sendMessage(ChatColor.YELLOW + "Hot Chunks:");
        if (reportDisabled) {
            sender.sendMessage(ChatColor.GRAY + "Chunk reporting disabled (max-entity-report=0).");
            return true;
        }
        if (topChunks.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "No loaded chunks with entities.");
            return true;
        }

        for (ChunkSnapshot entry : topChunks) {
            ChatColor severity = chunkSeverity(entry.getEntityCount(), chunkWarning);
            String warning = entry.getEntityCount() >= chunkWarning ? " (over " + chunkWarning + ")" : "";
            sender.sendMessage(severity + entry.getWorldName() + " (" + entry.getX() + ", " + entry.getZ() + "): "
                    + formatCount(entry.getEntityCount()) + " entities" + warning);
        }
        return true;
    }

    private List<Map.Entry<EntityType, Integer>> topEntities(Map<EntityType, Integer> counts, int maxReport) {
        if (maxReport <= 0) {
            return List.of();
        }
        List<Map.Entry<EntityType, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator.comparingInt((Map.Entry<EntityType, Integer> entry) -> entry.getValue()).reversed());
        if (entries.size() > maxReport) {
            return entries.subList(0, maxReport);
        }
        return entries;
    }

    private List<ChunkSnapshot> topChunks(List<ChunkSnapshot> snapshots, int maxReport) {
        if (maxReport <= 0) {
            return List.of();
        }
        snapshots.sort(Comparator.comparingInt(ChunkSnapshot::getEntityCount).reversed());
        if (snapshots.size() > maxReport) {
            return snapshots.subList(0, maxReport);
        }
        return snapshots;
    }

    private String formatCount(int count) {
        return NumberFormat.getIntegerInstance(Locale.US).format(count);
    }

    private ChatColor chunkSeverity(int count, int warning) {
        if (warning <= 0) {
            return ChatColor.GREEN;
        }
        if (count >= warning * 2L) {
            return ChatColor.RED;
        }
        if (count >= warning) {
            return ChatColor.YELLOW;
        }
        return ChatColor.GREEN;
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
}
