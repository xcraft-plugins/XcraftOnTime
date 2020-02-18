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
        return config.getString("PLUGINPREFIX");
    }

    String getPlayerName() {
        return config.getString("PLAYERNAME");
    }

    String getPlayTime() {
        return config.getString("PLAYTIME");
    }

    String getJoinDate() {
        return config.getString("JOINDATE");
    }

    String getDaysValue(){
        return config.getString("DAYS");
    }

    String getHoursValue(){
        return config.getString("HOURS");
    }

    String getMinutesValue(){
        return config.getString("MINUTES");
    }

    String getNotAnIntegerError() {
        return config.getString("NOTANNUMBER");
    }

    String getPlayerNotFoundError() {
        return config.getString("PLAYERNOTFOUND");
    }

    String getCommandIsMissingArgsError() {
        return config.getString("COMMANDISMISSINGARGS");
    }

    String getAmountHigherThanPlayTimeError() {
        return config.getString("AMOUNTHIGHERTHANPLAYTIME");
    }

    int getTopListMax(){
        return config.getInt("topListMax");
    }

    String getPlayerValues(){
        return config.getString("PLAYER");
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
        return config.getString("REMOVETIME");
    }

    public String ontimeAddUsage(){
        return config.getString("ADDTIME");
    }

    public String olddatabase(){
        return config.getString("oldmysql.database");
    }

    public String oldtable(){
        return config.getString("oldmysql.table");
    }

    public boolean convert(){
        return config.getBoolean("convert");
    }
}