package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.TimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class AfkStatusChange implements Listener {

    private final Logger logger;
    private final DatabaseHandler databaseHandler;
    private final TimeTracker timeTracker;
    private final ConfigHandler configHandler;

    public AfkStatusChange(
        Logger logger,
        TimeTracker timeTracker,
        DatabaseHandler databaseHandler,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.timeTracker = timeTracker;
        this.databaseHandler = databaseHandler;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onAfkStatusChangeEvent(AfkStatusChangeEvent event) {
        Player player = event.getAffected().getBase();
        boolean isAfk = event.getValue();
        if (isAfk) {
            databaseHandler.initPlayer(player);
            if (configHandler.isPluginDebugging()) {
                logger.info(
                    String.format(configHandler.playerIsNotAFK(), event.getAffected().getName())
                );
            }
        } else {
            timeTracker.savePlayerTime(player.getUniqueId());
            if (configHandler.isPluginDebugging()) {
                logger.info(
                    String.format(configHandler.playerIsAFK(), event.getAffected().getName())
                );
            }
        }
    }
}
