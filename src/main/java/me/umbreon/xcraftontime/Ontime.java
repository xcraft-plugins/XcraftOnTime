package me.umbreon.xcraftontime;

import me.umbreon.xcraftontime.converter.Converter;
import me.umbreon.xcraftontime.events.AfkStatusChange;
import me.umbreon.xcraftontime.events.PlayerJoin;
import me.umbreon.xcraftontime.events.PlayerQuit;
import me.umbreon.xcraftontime.utils.CommandHandler;
import me.umbreon.xcraftontime.utils.ConfigHandler;
import me.umbreon.xcraftontime.utils.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Ontime extends JavaPlugin {

    public DatabaseHandler databaseHandler;
    public ConfigHandler configHandler;
    public Converter converter;

    public void onEnable() { saveDefaultConfig();
        configHandler = new ConfigHandler(this);
        CommandHandler commandHandler = new CommandHandler(this);
        databaseHandler = new DatabaseHandler(this, configHandler);
        converter = new Converter(this, configHandler);
        databaseHandler.startup();
        databaseHandler.startTimedSaving();
        converter.convert();
        saveDefaultConfig();

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this, configHandler), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AfkStatusChange(this), this);
        getCommand("ontime").setExecutor(commandHandler);
    }

    public void onDisable() {
        databaseHandler.Shutdown();
    }

}