package me.umbreon.ontimetracker.events;

import me.umbreon.ontimetracker.OntimeTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private OntimeTracker main;

    public PlayerQuit(OntimeTracker ontimeTracker) {
        this.main = ontimeTracker;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        main.configHandler.PlayerQuit(event.getPlayer());
    }
}

