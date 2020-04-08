package me.umbreon.xcraftontime;

import me.umbreon.xcraftontime.events.AfkStatusChange;
import me.umbreon.xcraftontime.events.PlayerJoinListener;
import me.umbreon.xcraftontime.events.PlayerQuitListener;
import me.umbreon.xcraftontime.handlers.CommandHandler;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XcraftOnTimePlugin extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;
    private TimeHandler timeHandler;

    public void onEnable() {
        saveDefaultConfig();

        configHandler = new ConfigHandler(this);
        databaseHandler = new DatabaseHandler(getLogger(), configHandler);
        timeHandler = new TimeHandler(this, databaseHandler, configHandler);

        initDatabase();

        // register events
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(
            new PlayerJoinListener(getLogger(), databaseHandler, timeHandler, configHandler), this
        );
        pluginManager.registerEvents(
            new PlayerQuitListener(getLogger(), timeHandler, configHandler), this
        );
        pluginManager.registerEvents(
            new AfkStatusChange(getLogger(), timeHandler, databaseHandler, configHandler), this
        );

        // register command handler
        getCommand("ontime").setExecutor(
            new CommandHandler(getLogger(), databaseHandler, configHandler, timeHandler, this)
        );
    }

    private void initDatabase() {
        databaseHandler.startup();
        timeHandler.startTimedSaving();
    }

    public void onDisable() {
        timeHandler.saveAllPlayerTime();
        databaseHandler.closeConnection();
    }

}
