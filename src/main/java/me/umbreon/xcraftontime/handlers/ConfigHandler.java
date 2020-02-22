package me.umbreon.xcraftontime.handlers;

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

    public String getPluginPrefix() {
        return config.getString("PLUGINPREFIX");
    }

    public String getPlayerName() {
        return config.getString("PLAYERNAME");
    }

    public String getPlayTime() {
        return config.getString("PLAYTIME");
    }

    public String getJoinDate() {
        return config.getString("JOINDATE");
    }

    public String getDaysValue(){
        return config.getString("DAYS");
    }

    public String getHoursValue(){
        return config.getString("HOURS");
    }

    public String getMinutesValue(){
        return config.getString("MINUTES");
    }

    public Boolean isPluginDebugging(){
        return config.getBoolean("debug");
    }

    public int getTopListMax(){
        return config.getInt("topListMax");
    }

    public String getPlayerValues(){
        return config.getString("PLAYER");
    }



    public String ontimeRemoveUsage(){
        return config.getString("REMOVETIME");
    }

    public String ontimeAddUsage(){
        return config.getString("ADDTIME");
    }

    public boolean convert(){
        return config.getBoolean("convert");
    }


    //Everything for the converter:
    public String olddatabase(){
        return config.getString("oldmysql.database");
    }

    public String oldtable(){
        return config.getString("oldmysql.table");
    }


    //Everything for MySQL:
    public String getHostAdress(){
        return config.getString("mysql.host");
    }

    public String getDatabaseName(){
        return config.getString("mysql.database");
    }

    public String getPassword(){
        return config.getString("mysql.password");
    }

    public String getPort(){
        return config.getString("mysql.port");
    }

    public String getUsername(){
        return config.getString("mysql.user");
    }

    public String getTable(){
        return config.getString("mysql.table");
    }


    //Every error message:
    public String NotAnIntegerError() {
        return config.getString("NotAnInteger");
    }

    public String PlayerNotFoundError() {
        return config.getString("PlayerNotFound");
    }

    public String CommandIsMissingArgsError() {
        return config.getString("CommandIsMissingArgs");
    }

    public String AmountHigherThanPlaytimeError() {
        return config.getString("AmountHigherThanPlaytime");
    }

    public String NoConnectionToSQLError(){
        return config.getString("NoConnectionToSQL");
    }
}