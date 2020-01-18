package me.umbreon.ontimetracker;

import me.umbreon.ontimetracker.events.AfkStatusChange;
import me.umbreon.ontimetracker.events.PlayerJoin;
import me.umbreon.ontimetracker.events.PlayerQuit;
import me.umbreon.ontimetracker.utils.CommandHandler;
import me.umbreon.ontimetracker.utils.ConfigHandler;
import me.umbreon.ontimetracker.utils.DatabaseHandler;
import me.umbreon.ontimetracker.utils.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

//TODO: /ontime top - Command.
//TODO: Split files with 100+ lines up
//TODO: Add Permissions
//TODO: PlayerJoin add Sleeptimer
public class OntimeTracker extends JavaPlugin {

    public static Logger Logger;
    //public static final String BASEPATHFILE = OntimeTracker.PLUGIN.getDataFolder() + File.separator + "shops";
    public static final String BASEPATHFILE = System.getProperty("user.dir") + "/plugins/OntimeTracker/userfiles/"; //Todo: Change this shit

    public FileHandler fileHandler;
    public DatabaseHandler databaseHandler;
    public ConfigHandler configHandler;

    public void onEnable() {
        saveDefaultConfig();
        configHandler = new ConfigHandler(this);
        CommandHandler commandHandler = new CommandHandler(this);
        fileHandler = new FileHandler(this, configHandler);
        databaseHandler = new DatabaseHandler(this, configHandler);
        databaseHandler.sqlCreateConnection();
        databaseHandler.sqlCreateTable();


        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        //Bukkit.getServer().getPluginManager().registerEvents(new AfkStatusChange(this), this);
        getCommand("ontime").setExecutor(commandHandler);
    }

    public void onDisable() {
        databaseHandler.sqlSaveAll();
    }

}