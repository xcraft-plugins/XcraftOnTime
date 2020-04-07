package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private ConfigHandler configHandler;
    private TimeHandler timeHandler;

    public PlayerQuitListener(TimeHandler timeHandler, ConfigHandler configHandler) {
        this.timeHandler = timeHandler;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        timeHandler.savePlayerTime(event.getPlayer().getUniqueId());
        if (configHandler.isPluginDebugging()){
            Bukkit.getLogger().info(String.format(configHandler.PlayerQuit(), event.getPlayer().getName()));
        }
    }
}

