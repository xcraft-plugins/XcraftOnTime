package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.Ontime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private Ontime main;

    public PlayerQuit(Ontime ontime) {
        this.main = ontime;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        main.databaseHandler.MySqlQuitEvent(event.getPlayer().getUniqueId());
    }
}

