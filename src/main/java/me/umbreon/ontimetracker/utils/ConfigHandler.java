package me.umbreon.ontimetracker.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigHandler {

    JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public long getSleepTimer() {
        return config.getInt("SleepTimer");
    }

    String getPluginPrefix() {
        return config.getString("PluginPrefix");
    }

    String getPlayerName() {
        return config.getString("PlayerName");
    }

    String getPlayTime() {
        return config.getString("PlayTime");
    }

    String getJoinDate() {
        return config.getString("PlayerJoinDate");
    }

    String getDaysValue(){
        return config.getString("DaysValue");
    }

    String getHoursValue(){
        return config.getString("HoursValue");
    }

    String getMinutesValue(){
        return config.getString("MinutesValue");
    }

    String getNotAnIntegerError() {
        return config.getString("NotAnIntegerError");
    }

    String getPlayerNotFoundError() {
        return config.getString("PlayerNotFoundError");
    }

    String getCommandIsMissingArgsError() {
        return config.getString("CommandIsMissingArgsError");
    }

    String getAmountHigherThanPlayTimeError() {
        return config.getString("AmountHigherThanPlayTimeError");
    }

    String getDatabaseName() {
        return config.getString("DatabaseName");
    }

    String getTableName(){
        return config.getString("TableName");
    }
}