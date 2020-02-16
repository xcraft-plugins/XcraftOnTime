package me.umbreon.xcraftontime.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigHandler {

    public boolean getDebugValue;
    JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
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

    int getTopListMax(){
        return config.getInt("topListMax");
    }

    String getPlayerValues(){
        return config.getString("PlayerValue");
    }

    boolean getDebugValue() {
        return config.getBoolean("debug");
    }

    public String host(){
        return config.getString("mysql.host");
    }

    public String database(){
        return config.getString("mysql.database");
    }

    public String password(){
        return config.getString("mysql.password");
    }

    public String port(){
        return config.getString("mysql.port");
    }

    public String user(){
        return config.getString("mysql.user");
    }

    public String ontimeRemoveUsage(){
        return config.getString("OntimeRemoveUsage");
    }

    public String ontimeAddUsage(){
        return config.getString("OntimeAddUsage");
    }

    public String olddatabase(){
        return config.getString("oldmysql.database");
    }

    public String oldtable(){
        return config.getString("oldmysql.table");
    }
}