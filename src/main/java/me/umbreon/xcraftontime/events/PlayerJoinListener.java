package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;
import java.util.logging.Logger;

public class PlayerJoinListener implements Listener {

    private final Logger logger;
    private TimeHandler timeHandler;
    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;

    public PlayerJoinListener(
        Logger logger,
        DatabaseHandler databaseHandler,
        TimeHandler timeHandler,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.databaseHandler = databaseHandler;
        this.timeHandler = timeHandler;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        databaseHandler.initPlayer(event.getPlayer());
        timeHandler.putTimeInCache(event.getPlayer().getUniqueId(), Instant.now());
        if (configHandler.isPluginDebugging()) {
            logger.info(String.format(configHandler.PlayerJoin(), event.getPlayer().getName()));
        }
    }
}
