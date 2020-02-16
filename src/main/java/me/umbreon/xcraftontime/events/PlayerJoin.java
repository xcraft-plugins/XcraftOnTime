package me.umbreon.xcraftontime.events;

import me.umbreon.xcraftontime.Ontime;
import me.umbreon.xcraftontime.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Ontime main;
    private ConfigHandler config;

    public PlayerJoin(Ontime ontime, ConfigHandler configHandler) {
        main = ontime;
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
                    main.databaseHandler.MySqlJoinEvent(player);
                }

            }, (SleepTimer % 3600) / 60);

        } else {
            main.databaseHandler.MySqlJoinEvent(player);
        }
    }
}
