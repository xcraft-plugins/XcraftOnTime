package me.umbreon.xcraftontime;

import me.umbreon.xcraftontime.events.AfkStatusChange;
import me.umbreon.xcraftontime.events.PlayerListener;
import me.umbreon.xcraftontime.handlers.CommandHandler;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XcraftOnTimePlugin extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;
    private TimeTracker timeTracker;

    public void onEnable() {
        saveDefaultConfig();

        configHandler = new ConfigHandler(this);
        databaseHandler = new DatabaseHandler(getLogger(), configHandler);
        timeTracker = new TimeTracker(this, databaseHandler, configHandler);

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        EssentialsUtils essentials = new EssentialsUtils(pluginManager);

        initDatabase();

        // register events
        pluginManager.registerEvents(
            new PlayerListener(getLogger(), databaseHandler, timeTracker, configHandler), this
        );
        if(essentials.isEnabled()) {
            pluginManager.registerEvents(
                new AfkStatusChange(getLogger(), timeTracker, databaseHandler, configHandler), this
            );
        }

        // register command handler
        getCommand("ontime").setExecutor(
            new CommandHandler(getLogger(), databaseHandler, configHandler, timeTracker, this)
        );
    }

    private void initDatabase() {
        databaseHandler.startup();
        timeTracker.startTimedSaving();
    }

    public void onDisable() {
        timeTracker.saveAllPlayerTime();
        databaseHandler.closeConnection();
    }

}
