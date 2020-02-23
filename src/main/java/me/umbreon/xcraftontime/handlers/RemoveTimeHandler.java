package me.umbreon.xcraftontime.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveTimeHandler {

    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;

    public RemoveTimeHandler(ConfigHandler configHandler, DatabaseHandler databaseHandler) {
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
    }

    public void RemoveTime(CommandSender commandSender, OfflinePlayer offlinePlayer, int amount) {
        String update = "UPDATE " + configHandler.getTable() + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            if (!(amount > databaseHandler.getPlaytime(offlinePlayer.getUniqueId()))) {

                statement = databaseHandler.connection.prepareStatement(update);
                statement.setInt(1, databaseHandler.getPlaytime(offlinePlayer.getUniqueId()) - amount);
                statement.setString(2, offlinePlayer.getUniqueId().toString());
                statement.executeUpdate();

                if (amount > 1) {
                    commandSender.sendMessage(ChatColor.DARK_GRAY + "" + amount + " Sekunden wurden dem Spieler " + offlinePlayer.getName() + " abgezogen.");
                } else {
                    commandSender.sendMessage(ChatColor.DARK_GRAY + "" + amount + " Sekunde wurden dem Spieler " + offlinePlayer.getName() + " abgezogen.");
                }

                if (configHandler.isPluginDebugging()) {
                    Bukkit.getLogger().info(configHandler.getTable() + commandSender.getName() + " removed " + amount + " seconds of playtime from " + offlinePlayer.getName());
                }

            } else {
                commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.AmountHigherThanPlaytimeError());
            }

        } catch (SQLException e) {
            Bukkit.getLogger().info(e.toString());

            String error = configHandler.getPluginPrefix() + " " + configHandler.NoConnectionToSQLError();

            if (commandSender.isOp()) {
                commandSender.sendMessage(error);
            }
        }
    }
}
