package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeletePlayerCommand {

    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;

    public DeletePlayerCommand(OnlineTimeTracker onlineTimeTracker) {
        configHandler = onlineTimeTracker.getConfigHandler();
        databaseHandler = onlineTimeTracker.getDatabaseHandler();
    }

    public void executeRemovePlayerCommand(CommandSender commandSender, String[] args){
        if (commandSender.hasPermission("xcraftontime.delete")){
            String delete = "DELETE FROM " + configHandler.getTable() + " WHERE uuid = ?";
            PreparedStatement statement = null;
            try {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                statement = databaseHandler.connection.prepareStatement(delete);
                statement.setString(1, offlinePlayer.getUniqueId().toString());
                statement.executeUpdate();
                commandSender.sendMessage(configHandler.getPluginPrefix() + ChatColor.GRAY + " Die gespeicherten Daten vom Spieler " + offlinePlayer + " wurden erfolgreich gelöscht.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.PlayerNotFoundError());
        }

    }
}
