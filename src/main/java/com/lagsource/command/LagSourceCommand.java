package com.lagsource.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class LagSourceCommand implements CommandExecutor {
    private static final String PERMISSION_USE = "lagsource.use";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION_USE)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use LagSource.");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "LagSource Snapshot");
        sender.sendMessage(ChatColor.GRAY + "LagSource is not a profiler.");
        return true;
    }
}
