package me.umbreon.ontimetracker.events;

import me.umbreon.ontimetracker.OntimeTracker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {

    private OntimeTracker main;

    public PlayerJoin(OntimeTracker ontimeTracker) {
        main = ontimeTracker;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws InterruptedException {
        Bukkit.getLogger().info("=> Player Join Event executed."); //Todo: Remove this.
        UUID PlayerID = event.getPlayer().getUniqueId();
        String PlayerName = event.getPlayer().getName();
        if(!event.getPlayer().hasPlayedBefore()) {
            Thread.sleep(600000); //Todo: Add Times in Config.
            this.main.configHandler.PlayerJoined(PlayerName, PlayerID);
        } else {
            this.main.configHandler.PlayerJoined(PlayerName, PlayerID);
        }
    }
}
