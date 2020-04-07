package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

public class PlayerQuitListener implements Listener {

    private final Logger logger;
    private ConfigHandler configHandler;
    private TimeHandler timeHandler;

    public PlayerQuitListener(
        Logger logger,
        TimeHandler timeHandler,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.timeHandler = timeHandler;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        timeHandler.savePlayerTime(event.getPlayer().getUniqueId());
        if (configHandler.isPluginDebugging()) {
            logger.info(String.format(configHandler.PlayerQuit(), event.getPlayer().getName()));
        }
    }
}

