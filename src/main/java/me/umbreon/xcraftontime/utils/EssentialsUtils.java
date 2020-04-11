package me.umbreon.xcraftontime.utils;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class EssentialsUtils {
    private static final String ESSENTIALS_NAME = "Essentials";
    private Essentials essentialsPlugin;

    public EssentialsUtils(PluginManager pluginManager) {
        if(pluginManager.isPluginEnabled(ESSENTIALS_NAME)) {
            essentialsPlugin = (Essentials) pluginManager.getPlugin("Essentials");
        }
    }

    public boolean isEnabled() {
        return essentialsPlugin != null;
    }

    public boolean isPlayerAfk(Player player) {
        return essentialsPlugin.getUser(player).isAfk();
    }
}
