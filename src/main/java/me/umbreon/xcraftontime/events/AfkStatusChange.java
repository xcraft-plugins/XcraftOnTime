package me.umbreon.xcraftontime.events;

import com.earth2me.essentials.Essentials;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AfkStatusChange implements Listener {

    private DatabaseHandler databaseHandler;
    private TimeHandler timeHandler;
    private ConfigHandler configHandler;
    private static Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    public AfkStatusChange(TimeHandler timeHandler, DatabaseHandler databaseHandler, ConfigHandler configHandler) {
        this.timeHandler = timeHandler;
        this.databaseHandler = databaseHandler;
        this.configHandler = configHandler;
    }


    @EventHandler
    public void onAfkStatusChangeEvent(AfkStatusChangeEvent event) {
        if (essentials.getUser(event.getAffected().getBase()).isAfk()) {
            databaseHandler.initPlayer(event.getAffected().getBase());
            if (configHandler.isPluginDebugging()){
                Bukkit.getLogger().info(String.format(configHandler.playerIsNotAFK(), event.getAffected().getName()));
            }
        } else {
            timeHandler.savePlayerTime(event.getAffected().getBase().getUniqueId());
            if (configHandler.isPluginDebugging()){
                Bukkit.getLogger().info(String.format(configHandler.playerIsAFK(), event.getAffected().getName()));
            }
        }
    }
}
