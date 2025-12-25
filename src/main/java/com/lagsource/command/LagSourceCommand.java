package com.lagsource.command;

import com.lagsource.LagSourcePlugin;
import com.lagsource.snapshot.EntitySnapshot;
import com.lagsource.snapshot.EntitySnapshotService;
import org.bukkit.ChatColor;
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
        int maxReport = plugin.getConfig().getInt("max-entity-report", 5);

        EntitySnapshot snapshot = entitySnapshotService.snapshot(plugin.getServer().getWorlds(), includePlayers);
        List<Map.Entry<EntityType, Integer>> topEntities = topEntities(snapshot.getCounts(), maxReport);

        sender.sendMessage(ChatColor.GOLD + "LagSource Snapshot");
        sender.sendMessage(ChatColor.GRAY + "LagSource is not a profiler.");
        sender.sendMessage(ChatColor.YELLOW + "Total Entities: " + formatCount(snapshot.getTotal()));
        sender.sendMessage(ChatColor.YELLOW + "Top Entities:");
        if (topEntities.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "No loaded entities.");
            return true;
        }

        for (Map.Entry<EntityType, Integer> entry : topEntities) {
            sender.sendMessage(ChatColor.GRAY + entry.getKey().name() + ": " + formatCount(entry.getValue()));
        }
        return true;
    }

    private List<Map.Entry<EntityType, Integer>> topEntities(Map<EntityType, Integer> counts, int maxReport) {
        List<Map.Entry<EntityType, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator.comparingInt((Map.Entry<EntityType, Integer> entry) -> entry.getValue()).reversed());
        if (entries.size() > maxReport) {
            return entries.subList(0, maxReport);
        }
        return entries;
    }

    private String formatCount(int count) {
        return NumberFormat.getIntegerInstance(Locale.US).format(count);
    }
}
