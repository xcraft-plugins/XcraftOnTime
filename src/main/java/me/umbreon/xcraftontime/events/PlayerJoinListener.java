package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;

public class PlayerJoinListener implements Listener {

    private TimeHandler timeHandler;
    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;

    public PlayerJoinListener(DatabaseHandler databaseHandler, TimeHandler timeHandler, ConfigHandler configHandler) {
        this.databaseHandler = databaseHandler;
        this.timeHandler = timeHandler;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        databaseHandler.initPlayer(event.getPlayer());
        timeHandler.putTimeInCache(event.getPlayer().getUniqueId(), Instant.now());
        if (configHandler.isPluginDebugging()){
            Bukkit.getLogger().info(String.format(configHandler.PlayerJoin(), event.getPlayer().getName()));
        }
    }
}
