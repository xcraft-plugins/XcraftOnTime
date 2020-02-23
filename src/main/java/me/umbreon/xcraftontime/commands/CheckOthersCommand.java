package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CheckOthersCommand {

    private OnlineTimeTracker onlineTimeTracker;
    private ConfigHandler config;
    private DatabaseHandler database;

    public CheckOthersCommand( OnlineTimeTracker onlineTimeTracker ) {
        this.onlineTimeTracker = onlineTimeTracker;
        config = this.onlineTimeTracker.getConfigHandler();
        database = this.onlineTimeTracker.getDatabaseHandler();
    }

    public void checkOther(Player sender, OfflinePlayer target){
        if (sender.hasPermission("xcraftontime.check.others")){
            try {
                if (database.checkConnection()){
                    UUID uuid = target.getUniqueId();
                    Date joined = new Date(sender.getFirstPlayed());
                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    if (target.isOnline()){
                        int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - database.cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS) + database.getPlaytime(uuid));
                        long TotalDays = time / (24 * 3600);
                        time = time % (24 * 3600);
                        long TotalHours = time / 3600;
                        time %= 3600;
                        long TotalMins = time / 60 ;
                        sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayerName() + ChatColor.RESET + ChatColor.BOLD + " " + target.getName());
                        sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayTime() + " " + TotalDays + " " + config.getDaysValue() + " " + TotalHours + " " + config.getHoursValue() + " " + TotalMins + " " + config.getMinutesValue());
                        if (sender.hasPermission("xcraftontime.see.othersdate")) {
                            sender.sendMessage(config.getPluginPrefix() + " " + config.getJoinDate() + " " + DATE_FORMAT.format(joined));
                        }

                    } else {
                        long time = database.getPlaytime(uuid);
                        long TotalDays = time / (24 * 3600);
                        time = time % (24 * 3600);
                        long TotalHours = time / 3600;
                        time %= 3600;
                        long TotalMins = time / 60 ;
                        sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayerName() + ChatColor.RESET + ChatColor.BOLD + " " + target.getName());
                        sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayTime() + " " + TotalDays + config.getDaysValue() + " " + TotalHours + " " + config.getHoursValue() + " " + TotalMins + " " + config.getMinutesValue());
                        if (sender.hasPermission("xcraftontime.see.othersdate")) {
                            sender.sendMessage(config.getPluginPrefix() + " " + config.getJoinDate() + " " + DATE_FORMAT.format(joined));
                        }
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[XCraftOntime] Couldn't connect to database!");
            }
        } else {
            sender.sendMessage(config.getPluginPrefix() + config.NoPermissionError());
        }
    }

}

