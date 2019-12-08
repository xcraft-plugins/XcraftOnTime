package me.umbreon.ontimetracker;

import me.umbreon.ontimetracker.events.PlayerJoin;
import me.umbreon.ontimetracker.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class OntimeTracker extends JavaPlugin {

    public static final String BASEPATHFILE = System.getProperty("user.dir") + "/plugins/OntimeTracker/userfiles/";
    public ConfigHandler configHandler = new ConfigHandler();

    public void onEnable() {
        dirCheck();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        createFolder();
    }

    public void onDisable() {

    }

    private void dirCheck(){
        File PluginFolder = new File(System.getProperty("user.dir") + "/plugins/OntimeTracker");
        if (!PluginFolder.exists()) { boolean success = PluginFolder.mkdir(); }
    }

    private void createFolder() {
        File PluginFolder = new File(BASEPATHFILE);

        if (!PluginFolder.exists()) {
            PluginFolder.mkdir();
            Bukkit.getLogger().info("Created folder for Ontime.");
        }
    }
}
