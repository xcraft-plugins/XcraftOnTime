package me.umbreon.ontimetracker.events;

import com.earth2me.essentials.Essentials;
import me.umbreon.ontimetracker.OntimeTracker;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AfkStatusChange implements Listener {

    private OntimeTracker main;
    private static Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    public AfkStatusChange(OntimeTracker ontimeTracker) {
        main = ontimeTracker;
    }

    @EventHandler
    public void onAfkStatusChangeEvent(AfkStatusChangeEvent event){
        if (essentials.getUser(event.getAffected().getBase()).isAfk()){
            main.databaseHandler.PlayerJoined(event.getAffected().getBase());
        } else {
            main.databaseHandler.PlayerQuitted(event.getAffected().getBase().getUniqueId());
        }
    }
}
