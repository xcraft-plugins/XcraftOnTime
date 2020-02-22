package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.Ontime;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerQuit implements Listener {
    private Ontime main;
    private ConfigHandler config;
    private DatabaseHandler database;

    public PlayerQuit(Ontime ontime, DatabaseHandler databaseHandler) {
        this.main = ontime;
        config = main.getConfigHandler();
        database = databaseHandler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        quitEvent(event.getPlayer().getUniqueId());
    }

    public void quitEvent(UUID uuid) {
        String update = "UPDATE " + config.getTable() + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            if (database.checkConnection()){
                statement = database.connection.prepareStatement(update);
                int playedTime;
                if (database.cache.get(uuid) == null){
                    playedTime = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime(), TimeUnit.MILLISECONDS));
                } else {
                    playedTime = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - database.cache.get(uuid).getTime(), TimeUnit.MILLISECONDS));
                }
                statement.setInt(1, database.getPlaytime(uuid) + playedTime);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("[XCraftOntime] Couldn't connect to database!");
        }
    }
}

