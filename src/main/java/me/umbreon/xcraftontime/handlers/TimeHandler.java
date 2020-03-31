package me.umbreon.xcraftontime.handlers;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimeHandler {

    private OnlineTimeTracker onlineTimeTracker;
    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;

    public Map<UUID, Instant> cache = new HashMap<>();

    public TimeHandler(OnlineTimeTracker onlineTimeTracker, DatabaseHandler databaseHandler, ConfigHandler configHandler){
        this.onlineTimeTracker = onlineTimeTracker;
        this.databaseHandler = databaseHandler;
        this.configHandler = configHandler;
    }

    public void putTimeInCache(UUID uuid, Instant date){
        cache.put(uuid, date);
    }

    public void savePlayerTime(UUID uuid){
        long playedTime;
        if (cache.get(uuid) == null) {
            playedTime = 0;
        } else {
            playedTime = ((int) TimeUnit.SECONDS.convert(Instant.now().toEpochMilli() - cache.get(uuid).toEpochMilli(), TimeUnit.MILLISECONDS));
        }
        databaseHandler.addPlayerOnlineTime(uuid, playedTime);
        putTimeInCache(uuid, Instant.now());
    }

    public void startTimedSaving() {
        int SleepTimer = (int) configHandler.autoSavePeriod();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(onlineTimeTracker, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    savePlayerTime( player.getUniqueId());
                }
            }
        }, 0L, 20L * SleepTimer * 60);
    }

    public void saveAllPlayerTime(){
        for (UUID uuid : cache.keySet()) {
            savePlayerTime(uuid);
        }
    }
}
