package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.TimeTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;
import java.util.logging.Logger;

public class PlayerListener implements Listener {

    private final Logger logger;
    private final TimeTracker timeTracker;
    private final DatabaseHandler databaseHandler;
    private final ConfigHandler configHandler;

    public PlayerListener(
        Logger logger,
        DatabaseHandler databaseHandler,
        TimeTracker timeTracker,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.databaseHandler = databaseHandler;
        this.timeTracker = timeTracker;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        databaseHandler.initPlayer(event.getPlayer());
        timeTracker.putTimeInCache(event.getPlayer().getUniqueId(), Instant.now());
        if (configHandler.isPluginDebugging()) {
            logger.info(String.format(configHandler.PlayerJoin(), event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        timeTracker.savePlayerTime(event.getPlayer().getUniqueId());
        if (configHandler.isPluginDebugging()) {
            logger.info(String.format(configHandler.PlayerQuit(), event.getPlayer().getName()));
        }
    }
}
