package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ReloadCommand {

    private final Logger logger;
    private OnlineTimeTracker onlineTimeTracker;
    private ConfigHandler configHandler;

    public ReloadCommand(
        Logger logger,
        OnlineTimeTracker onlineTimeTracker,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.onlineTimeTracker = onlineTimeTracker;
        this.configHandler = configHandler;
    }

    public void reloadConfig(Player player) {
        if (player.hasPermission("xcraftontime.reload")) {
            onlineTimeTracker.reloadConfig();
            player.sendMessage(configHandler.configReloaded());
            if (configHandler.isPluginDebugging()) {
                logger.info("[" + configHandler.pluginPrefixString() + "] Reloading config.");
            }
        }
    }
}
