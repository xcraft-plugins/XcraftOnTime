package me.umbreon.xcraftontime.events;

import com.earth2me.essentials.Essentials;
import me.umbreon.xcraftontime.Ontime;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AfkStatusChange implements Listener {

    private Ontime main;
    private static Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    public AfkStatusChange(Ontime ontime) {
        main = ontime;
    }

    @EventHandler
    public void onAfkStatusChangeEvent(AfkStatusChangeEvent event){
        if (essentials.getUser(event.getAffected().getBase()).isAfk()){
            main.databaseHandler.MySqlJoinEvent(event.getAffected().getBase());
        } else {
            main.databaseHandler.MySqlQuitEvent(event.getAffected().getBase().getUniqueId());
        }
    }
}
