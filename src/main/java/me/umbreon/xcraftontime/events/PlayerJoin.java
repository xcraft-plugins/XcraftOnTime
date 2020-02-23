package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class PlayerJoin implements Listener {

    private OnlineTimeTracker onlineTimeTracker;
    private ConfigHandler config;
    private DatabaseHandler database;

    public PlayerJoin(OnlineTimeTracker onlineTimeTracker, ConfigHandler configHandler, DatabaseHandler databaseHandler) {
        this.onlineTimeTracker = onlineTimeTracker;
        this.config = configHandler;
        database = databaseHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int SleepTimer = (int) config.getSleepTimer();
        final Player player = event.getPlayer();

        if(!event.getPlayer().hasPlayedBefore()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(onlineTimeTracker, new Runnable() {

                @Override
                public void run() {
                    joinEvent(player);
                }

            }, (SleepTimer % 3600) / 60);

        } else {
            joinEvent(player);
        }
    }

    public void joinEvent(Player player){
        try {
            String select = "SELECT uuid FROM " + config.getTable() + " WHERE uuid = ?";
            if (database.checkConnection()){
                PreparedStatement statement = null;
                statement = database.connection.prepareStatement(select);
                statement.setString(1, player.getUniqueId().toString());
                ResultSet rs = statement.executeQuery();
                if (!rs.next()){
                    database.createNewEntry(player, 0);
                    database.cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
                } else {
                    database.cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.toString());
        }
    }
}
