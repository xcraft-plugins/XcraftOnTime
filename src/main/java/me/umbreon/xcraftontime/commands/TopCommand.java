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

public class TopCommand {

    private OnlineTimeTracker onlineTimeTracker;
    private ConfigHandler config;
    private DatabaseHandler database;

    public TopCommand( OnlineTimeTracker onlineTimeTracker ) {
        this.onlineTimeTracker = onlineTimeTracker;
        config = this.onlineTimeTracker.getConfigHandler();
        database = this.onlineTimeTracker.getDatabaseHandler();
    }

    public void showTopList(CommandSender commandSender){
        if (commandSender.hasPermission("xcraftontime.top")){
            String select  = "SELECT playtime, uuid FROM " + config.getTable() + " ORDER BY playtime DESC";
            PreparedStatement statement = null;
            try {
                statement = database.connection.prepareStatement(select);
                ResultSet rs = statement.executeQuery();
                int count = 0;
                int stat = 1;
                int top10Max = config.getTopListMax();
                commandSender.sendMessage(config.getPluginPrefix() + " Spielzeitstatistik - Top " + top10Max + " " + config.getPlayerValues());
                while (rs.next()){
                    if (count<top10Max){
                        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                        String name = player.getName();
                        int time = rs.getInt("playtime");
                        long days = time / (24 * 3600);
                        time = time % (24 * 3600);
                        long hours = time / 3600;
                        time %= 3600;
                        long mins = time / 60 ;

                        if (!(stat<10)) {
                            commandSender.sendMessage(stat + ": " + ChatColor.GOLD + name + " " + ChatColor.AQUA + days + " " + config.getDaysValue() + " " + hours + " " + config.getHoursValue() + " " + mins + config.getMinutesValue());
                        } else {
                            commandSender.sendMessage("  " + stat + ": " + ChatColor.GOLD + name + " " + ChatColor.AQUA + days + " " + config.getDaysValue() + " " + hours + " " + config.getHoursValue() + " " + mins + " " + config.getMinutesValue());
                        }

                        count++;
                        stat++;
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info( e.toString() );

                String error = config.getPluginPrefix() + " " + config.NoConnectionToSQLError();

                if (commandSender.isOp()){
                    commandSender.sendMessage(error);
                }
            }
        } else {
            commandSender.sendMessage(config.getPluginPrefix() + config.PlayerNotFoundError());
        }
    }
}
