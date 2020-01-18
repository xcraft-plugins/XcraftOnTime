package me.umbreon.ontimetracker.events;

import me.umbreon.ontimetracker.OntimeTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private OntimeTracker main;

    public PlayerJoin(OntimeTracker ontimeTracker) {
        main = ontimeTracker;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        main.databaseHandler.sqlPlayerJoin(player);


        //if(!event.getPlayer().hasPlayedBefore()) {
        //    Bukkit.getLogger().info("=> Thread.sleep"); //Todo: Remove this.
        //    //TODO: add delay
        //    Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
        //        @Override
        //        public void run() {
        //            main.fileHandler.filePlayerJoined(player);
        //        }
        //    }, 600000);
        //} else {
        //    main.fileHandler.filePlayerJoined(player);
        //    Bukkit.getLogger().info("=> No Thread.sleep"); //Todo: Remove this.
        //}
    }
}
