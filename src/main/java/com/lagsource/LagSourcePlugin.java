package com.lagsource;

import com.lagsource.command.LagSourceCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class LagSourcePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getCommand("lagsource") != null) {
            getCommand("lagsource").setExecutor(new LagSourceCommand());
        } else {
            getLogger().severe("Command 'lagsource' is missing from plugin.yml.");
        }
    }
}
