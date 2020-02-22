package me.umbreon.xcraftontime.events;

import com.earth2me.essentials.Essentials;
import me.umbreon.xcraftontime.Ontime;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AfkStatusChange implements Listener {

    private Ontime main;
    private PlayerQuit PlayerQuit;
    private PlayerJoin PlayerJoin;
    private static Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    public AfkStatusChange(Ontime ontime) {
        main = ontime;
        PlayerQuit = new PlayerQuit(ontime, ontime.getDatabaseHandler());
        PlayerJoin = new PlayerJoin(ontime, main.getConfigHandler(), ontime.getDatabaseHandler());
    }

    @EventHandler
    public void onAfkStatusChangeEvent(AfkStatusChangeEvent event){
        if (essentials.getUser(event.getAffected().getBase()).isAfk()){
            PlayerJoin.joinEvent(event.getAffected().getBase());

        } else {
            PlayerQuit.quitEvent(event.getAffected().getBase().getUniqueId());
        }
    }
}
