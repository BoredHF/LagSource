package com.lagsource;

import com.lagsource.command.LagSourceCommand;
import com.lagsource.gui.MenuRouter;
import org.bukkit.plugin.java.JavaPlugin;

public final class LagSourcePlugin extends JavaPlugin {
    private final MenuRouter menuRouter = new MenuRouter();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getCommand("lagsource") != null) {
            getCommand("lagsource").setExecutor(new LagSourceCommand(this, menuRouter));
        } else {
            getLogger().severe("Command 'lagsource' is missing from plugin.yml.");
        }
        getServer().getPluginManager().registerEvents(menuRouter, this);
    }
}
