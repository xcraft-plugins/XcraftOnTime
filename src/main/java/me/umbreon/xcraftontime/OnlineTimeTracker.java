package me.umbreon.xcraftontime;

import me.umbreon.xcraftontime.events.AfkStatusChange;
import me.umbreon.xcraftontime.events.PlayerJoinListener;
import me.umbreon.xcraftontime.events.PlayerQuitListener;
import me.umbreon.xcraftontime.handlers.CommandHandler;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class OnlineTimeTracker extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;
    private TimeHandler timeHandler;

    public void onEnable() {
        saveDefaultConfig();

        configHandler = new ConfigHandler(this);
        databaseHandler = new DatabaseHandler(configHandler);
        timeHandler = new TimeHandler(this, databaseHandler, configHandler);

        initDatabase();

        registerEvents();
        setCommandExecutor();

    }

    private void initDatabase() {
        databaseHandler.startup();
        timeHandler.startTimedSaving();
    }

    private void setCommandExecutor() {
        getCommand("ontime").setExecutor(new CommandHandler(databaseHandler, configHandler, timeHandler, this));
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(databaseHandler, timeHandler, configHandler), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(timeHandler, configHandler), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AfkStatusChange(timeHandler, databaseHandler, configHandler), this);
    }

    public void onDisable() {
        timeHandler.saveAllPlayerTime();
        databaseHandler.closeConnection();
    }

}