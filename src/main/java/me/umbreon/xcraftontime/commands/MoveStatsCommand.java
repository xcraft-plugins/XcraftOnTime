package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MoveStatsCommand {

    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;

    public MoveStatsCommand(OnlineTimeTracker onlineTimeTracker) {
        configHandler = onlineTimeTracker.getConfigHandler();
        databaseHandler = onlineTimeTracker.getDatabaseHandler();
    }

    public void executeMoveStatsCommand(CommandSender commandSender, String[] args){
        if (commandSender.hasPermission("xcraftontime.movestats")){
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            moveOnlineTime(commandSender, player, target);
        } else {
            commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.PlayerNotFoundError());
        }

    }

    private void moveOnlineTime(CommandSender commandSender, OfflinePlayer player, OfflinePlayer target){
        try {
            if (wasPlayerOnServer(target)){
                String update = "UPDATE " + configHandler.getTable() + " SET playtime = ? WHERE uuid = ?";
                PreparedStatement statement = null;
                statement = databaseHandler.connection.prepareStatement(update);
                statement.setInt(1, databaseHandler.getPlaytime(player.getUniqueId()));
                statement.setString(2, String.valueOf(target.getUniqueId()));
                statement.executeUpdate();
                commandSender.sendMessage(configHandler.getPluginPrefix() +
                        ChatColor.GRAY +
                        " Der Spieler " +
                        target +
                        " besitzt nun die Spielzeit vom Spieler " +
                        player);
            } else {
                commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.PlayerNotFoundError());
            }
            removePlayerEntry(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Boolean wasPlayerOnServer(OfflinePlayer target){
        Boolean WasPlayerOnServer = null;
        try {
            String select = "SELECT uuid FROM " + configHandler.getTable() + " WHERE uuid = ?";
            PreparedStatement statement = null;
            statement = databaseHandler.connection.prepareStatement(select);
            statement.setString(1, target.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            statement.executeUpdate();
            WasPlayerOnServer = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return WasPlayerOnServer;
    }

    private void removePlayerEntry(UUID uuid){
        try {
            String update = "UPDATE " + configHandler.getTable() + " SET playtime = ? WHERE uuid = ?";
            PreparedStatement preparedStatement = null;
            preparedStatement = databaseHandler.connection.prepareStatement(update);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, String.valueOf(uuid));
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
