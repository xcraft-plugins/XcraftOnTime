package me.umbreon.ontimetracker.events;

import me.umbreon.ontimetracker.OntimeTracker;
import me.umbreon.ontimetracker.utils.ConfigHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private OntimeTracker main;
    private ConfigHandler config;

    public PlayerJoin(OntimeTracker ontimeTracker, ConfigHandler configHandler) {
        main = ontimeTracker;
        this.config = configHandler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int SleepTimer = (int) config.getSleepTimer();
        final Player player = event.getPlayer();

        if(!event.getPlayer().hasPlayedBefore()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

                @Override
                public void run() {
                    main.databaseHandler.sqlPlayerJoin(player);
                }

            }, (SleepTimer % 3600) / 60);

        } else {
            main.databaseHandler.sqlPlayerJoin(player);
        }
    }
}
