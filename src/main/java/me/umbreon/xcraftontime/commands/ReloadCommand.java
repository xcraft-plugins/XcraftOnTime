package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReloadCommand {

    private OnlineTimeTracker onlineTimeTracker;
    private ConfigHandler configHandler;

    public ReloadCommand(OnlineTimeTracker onlineTimeTracker, ConfigHandler configHandler) {
        this.onlineTimeTracker = onlineTimeTracker;
        this.configHandler = configHandler;
    }

    public void reloadConfig(Player player) {
        if (player.hasPermission("xcraftontime.reload")){
            onlineTimeTracker.reloadConfig();
            player.sendMessage(configHandler.configReloaded());
            if (configHandler.isPluginDebugging()){
                Bukkit.getLogger().info("[" + configHandler.pluginPrefixString() + "] Reloading config.");
            }
        }
    }
}