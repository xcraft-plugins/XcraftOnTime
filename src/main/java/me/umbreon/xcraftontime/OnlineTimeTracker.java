package me.umbreon.xcraftontime;

import me.umbreon.xcraftontime.converter.Converter;
import me.umbreon.xcraftontime.events.AfkStatusChange;
import me.umbreon.xcraftontime.events.PlayerJoin;
import me.umbreon.xcraftontime.events.PlayerQuit;
import me.umbreon.xcraftontime.handlers.CommandHandler;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ToDo: produktbeschreibung
 */
public class OnlineTimeTracker extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;
    private Converter converter;

    public void onEnable() {
        saveDefaultConfig();

        setConfigHandler(new ConfigHandler(this));
        setDatabaseHandler(new DatabaseHandler(this, getConfigHandler()));
        setConverter(new Converter(this, getConfigHandler()));

        initDatebase();

        registerEvents();
        setExecutors();
    }

    private void initDatebase() {
        getDatabaseHandler().startup();
        getDatabaseHandler().startTimedSaving();
        getConverter().convert();
    }

    private void setExecutors() {
        getCommand("ontime").setExecutor(new CommandHandler(this));
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this, getConfigHandler(), getDatabaseHandler()), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuit(this, getDatabaseHandler()), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AfkStatusChange(this), this);
    }

    public void onDisable() {
        getDatabaseHandler().Shutdown();
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public void setConfigHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
}