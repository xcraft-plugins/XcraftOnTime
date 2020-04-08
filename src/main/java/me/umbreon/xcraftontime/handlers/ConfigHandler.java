package me.umbreon.xcraftontime.handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigHandler {

    private final FileConfiguration configuration;

    public ConfigHandler(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.configuration = plugin.getConfig();
    }

    public long autoSavePeriod() {
        return configuration.getInt("autoSavePeriod");
    }

    public Boolean isPluginDebugging() {
        return configuration.getBoolean("debug");
    }

    public int getTopListMax() {
        return configuration.getInt("topListMax");
    }

    // Strings

    public String timeString() {
        return configuration.getString("Time");
    }

    public String pluginPrefixString() {
        return configuration.getString("PluginPrefix");
    }

    public String playerString() {
        return configuration.getString("Player");
    }

    public String secondsString() {
        return configuration.getString("Seconds");
    }

    public String secondString() {
        return configuration.getString("Second");
    }

    public String minutesString() {
        return configuration.getString("Minutes");
    }

    public String hoursString() {
        return configuration.getString("Hours");
    }

    public String daysString() {
        return configuration.getString("Days");
    }

    public String helpPageString() {
        return configuration.getString("HelpPage");
    }

    public String statisticsString() {
        return configuration.getString("Statistic");
    }

    public String nameString() {
        return configuration.getString("Name");
    }

    public String playtimeString() {
        return configuration.getString("Playtime");
    }

    public String joinedAtString() {
        return configuration.getString("Joined");
    }

    // MySQL Connection Details

    public String getHostAdress() {
        return configuration.getString("mysql.host");
    }

    public String getDatabaseName() {
        return configuration.getString("mysql.database");
    }

    public String getPassword() {
        return configuration.getString("mysql.password");
    }

    public String getPort() {
        return configuration.getString("mysql.port");
    }

    public String getUsername() {
        return configuration.getString("mysql.user");
    }

    public String getTable() {
        return configuration.getString("mysql.table");
    }

    // Information-Messages

    public String AddTimeMessage() {
        return configuration.getString("AddTimeMessage");
    }

    public String ClearMessage() {
        return configuration.getString("ClearMessage");
    }

    public String RemoveTimeMessage() {
        return configuration.getString("RemoveTimeMessage");
    }

    public String ConnectionToSQLClosedMessage() {
        return configuration.getString("ConnectionToSQLClosed");
    }

    // Error-Messages:

    public String NotAnIntegerError() {
        return configuration.getString("NotAnIntegerError");
    }

    public String CommandIsMissingArgsError() {
        return configuration.getString("CommandIsMissingArgsError");
    }

    public String PlayerNotFoundError() {
        return configuration.getString("PlayerNotFoundError");
    }

    public String ExecuteError() {
        return configuration.getString("ExecuteError");
    }

    public String AmountHigherThanPlaytimeError() {
        return configuration.getString("AmountHigherThanPlaytimeError");
    }

    public String NoPermissionError() {
        return configuration.getString("NoPermissionError");
    }

    public String NoConnectionToSQLError() {
        return configuration.getString("NoConnectionToSQLError");
    }

    // Debug-Messages:

    public String PlayerJoin() {
        return configuration.getString("PlayerJoin");
    }

    public String PlayerQuit() {
        return configuration.getString("PlayerQuit");
    }

    public String AddTime() {
        return configuration.getString("AddTime");
    }

    public String Clear() {
        return configuration.getString("Clear");
    }

    public String RemoveTime() {
        return configuration.getString("RemoveTime");
    }

    public String playerIsAFK() {
        return configuration.getString("PlayerIsAFK");
    }

    public String playerIsNotAFK() {
        return configuration.getString("PlayerIsNotAFK");
    }

    public String configReloaded() {
        return configuration.getString("ConfigReloaded");
    }

    // Help-Messages:

    public String CheckOthersCommandHelpMessage() {
        return configuration.getString("CheckOthersCommandHelpMessage");
    }

    public String CheckCommandHelpMessage() {
        return configuration.getString("CheckCommandHelpMessage");
    }

    public String TopCommandHelpMessage() {
        return configuration.getString("TopCommandHelpMessage");
    }

    public String AddTimeCommandHelpMessage() {
        return configuration.getString("AddTimeCommandHelpMessage");
    }

    public String RemoveTimeCommandHelpMessage() {
        return configuration.getString("RemoveTimeCommandHelpMessage");
    }

    public String ClearCommandHelpMessage() {
        return configuration.getString("ClearCommandHelpMessage");
    }

    public String ReloadHelpMessage() {
        return configuration.getString("ReloadHelpMessage");
    }

    // Warnings:

    public String NoUndoWarning() {
        return configuration.getString("NoUndoWarning");
    }
}
