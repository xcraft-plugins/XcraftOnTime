package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CheckCommand {

    private OnlineTimeTracker onlineTimeTracker;
    private ConfigHandler config;
    private DatabaseHandler database;

    public CheckCommand( OnlineTimeTracker onlineTimeTracker ) {
        this.onlineTimeTracker = onlineTimeTracker;
        config = this.onlineTimeTracker.getConfigHandler();
        database = this.onlineTimeTracker.getDatabaseHandler();
    }

    public void checkPlayerCommand( Player sender ){
        if (sender.hasPermission("xcraftontime.check")){
            try {
                if ( database.checkConnection() ) {
                    UUID uuid = sender.getUniqueId();
                    int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - onlineTimeTracker.getDatabaseHandler().cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS) + onlineTimeTracker.getDatabaseHandler().getPlaytime(uuid));
                    long TotalDays = time / (24 * 3600);
                    time = time % (24 * 3600);
                    long TotalHours = time / 3600;
                    time %= 3600;
                    long TotalMins = time / 60;

                    Date joined = new Date(sender.getFirstPlayed());
                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    sender.sendMessage( config.getPluginPrefix() + " " + config.getPlayerName() + " " + sender.getName());
                    sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayTime() + " " + TotalDays + " " + config.getDaysValue() + " " + TotalHours + " " + config.getHoursValue() + " " + TotalMins + " " + config.getMinutesValue());

                    if (sender.hasPermission("xcraftontime.seedate")) {
                        sender.sendMessage(config.getPluginPrefix() + " " + config.getJoinDate() + " " + DATE_FORMAT.format(joined));
                    }

                }

            } catch (SQLException e) {
                Bukkit.getLogger().info( e.toString() );

                String error = config.getPluginPrefix() + " " + config.NoConnectionToSQLError();

                if (sender.isOp()){
                    sender.sendMessage(error);
                }
            }
        } else {
            sender.sendMessage(config.getPluginPrefix() + config.NoPermissionError());
        }
    }
}
