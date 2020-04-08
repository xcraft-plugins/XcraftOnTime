package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.XcraftOnTimePlugin;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ReloadCommand {

    private final Logger logger;
    private final XcraftOnTimePlugin plugin;
    private final ConfigHandler configHandler;

    public ReloadCommand(
        Logger logger,
        XcraftOnTimePlugin plugin,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.plugin = plugin;
        this.configHandler = configHandler;
    }

    public void reloadConfig(Player player) {
        if (player.hasPermission("xcraftontime.reload")) {
            plugin.reloadConfig();
            player.sendMessage(configHandler.configReloaded());
            if (configHandler.isPluginDebugging()) {
                logger.info("[" + configHandler.pluginPrefixString() + "] Reloading config.");
            }
        }
    }
}
