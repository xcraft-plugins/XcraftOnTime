package me.umbreon.ontimetracker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.umbreon.ontimetracker.OntimeTracker;
import me.umbreon.ontimetracker.model.UserData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FileHandler {

    private static String BASEPATHFILE = OntimeTracker.BASEPATHFILE;
    private ConfigHandler config;
    private HashMap<UUID, Date> cache = new HashMap<UUID, Date>();

    JavaPlugin plugin;

    public FileHandler(JavaPlugin plugin, ConfigHandler configHandler) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = configHandler;
    }

    public void filePlayerJoined(Player player) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File userFile = new File(BASEPATHFILE + player.getUniqueId() + ".yml");
        if (!userFile.exists()) {
            UserData userdata = new UserData(0, getJoinedDate(player));
            try {
                mapper.writeValue(new File(BASEPATHFILE + player.getUniqueId() + ".yml"), userdata);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
        } else {
            cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
        }
    }

    public void filePlayerQuit(UUID uuid) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UserData userdata = null;
        try {
            userdata = mapper.readValue(new File(BASEPATHFILE + uuid + ".yml"), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int playedTime = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(uuid).getTime(), TimeUnit.MILLISECONDS));
        userdata.setPlayTime(playedTime + userdata.getPlayTime());
        try {
            mapper.writeValue(new File(BASEPATHFILE + uuid + ".yml"), userdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileCheckPlayer(Player sender) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UserData userdata = null;
        try {
            userdata = mapper.readValue(new File(BASEPATHFILE + sender.getUniqueId() + ".yml"), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int playTimeWithoutTimeInConfig = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS));
        int playTimeWithTimeInConfig = (int) (playTimeWithoutTimeInConfig + userdata.getPlayTime());
        int TotalDays = playTimeWithTimeInConfig / (24 * 3600);
        playTimeWithTimeInConfig = playTimeWithTimeInConfig % (24 * 3600);
        int TotalHours = playTimeWithTimeInConfig / 3600;
        playTimeWithTimeInConfig %= 3600;
        int TotalMins = playTimeWithTimeInConfig / 60 ;
        sender.sendMessage(config.getPluginPrefix() + config.getPlayerName() + sender.getDisplayName());
        sender.sendMessage(config.getPluginPrefix() + config.getPlayTime() + TotalDays + config.getDaysValue() + TotalHours + config.getHoursValue() + TotalMins + config.getMinutesValue());
        if (sender.hasPermission("ontimetracker.see.mydate")) {
            sender.sendMessage(config.getPluginPrefix() + config.getJoinDate() + userdata.getJoinedDate());
        }
    }

    public void fileCheckTarget(Player sender, OfflinePlayer target) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UserData userdata = null;
        try {
            userdata = mapper.readValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (target.isOnline()){
            int playTimeWithoutTimeInConfig = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(target.getUniqueId()).getTime(), TimeUnit.MILLISECONDS));
            int playTimeWithTimeInConfig = (int) (playTimeWithoutTimeInConfig + userdata.getPlayTime());
            int TotalDays = playTimeWithTimeInConfig / (24 * 3600);
            playTimeWithTimeInConfig = playTimeWithTimeInConfig % (24 * 3600);
            int TotalHours = playTimeWithTimeInConfig / 3600;
            playTimeWithTimeInConfig %= 3600;
            int TotalMins = playTimeWithTimeInConfig / 60 ;
            sender.sendMessage(config.getPluginPrefix() + config.getPlayerName() + target.getName());
            sender.sendMessage(config.getPluginPrefix() + config.getPlayTime() + TotalDays + config.getDaysValue() + TotalHours + config.getHoursValue() + TotalMins + config.getMinutesValue());
            if (sender.hasPermission("ontimetracker.see.othersdate")) {
                sender.sendMessage(config.getPluginPrefix() + config.getJoinDate() + userdata.getJoinedDate());
            }

        } else {
            int playedTime = (int) userdata.getPlayTime();
            int CurrentDays = playedTime / (24 * 3600);
            playedTime = playedTime % (24 * 3600);
            int CurrentHours = playedTime / 3600;
            playedTime %= 3600;
            int CurrentMins = playedTime / 60 ;
            sender.sendMessage(config.getPluginPrefix() + config.getPlayerName() + target.getName());
            sender.sendMessage(config.getPluginPrefix() + config.getPlayTime() + CurrentDays + config.getDaysValue() + CurrentHours + config.getHoursValue() + CurrentMins + config.getMinutesValue());
            if (sender.hasPermission("ontimetracker.see.othersdate")) {
                sender.sendMessage(config.getPluginPrefix() + config.getJoinDate() + userdata.getJoinedDate());
            }
        }
    }

    public void fileAddTime(Player sender, OfflinePlayer target, int amount){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UserData userdata = null;
        try {
            userdata = mapper.readValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (target.isOnline()){
            filePlayerQuit(target.getUniqueId());
            userdata.setPlayTime(userdata.getPlayTime() + (amount * 60));
            try {
                mapper.writeValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), userdata);
            } catch (IOException e) {
                e.printStackTrace();
            }
            filePlayerJoined((Player) target);
            sender.sendMessage(config.getPluginPrefix() + ChatColor.GRAY + "Added " + amount + " minutes playtime to " + target.getName());

        } else {
            userdata.setPlayTime(userdata.getPlayTime() + (amount * 60));
            try {
                mapper.writeValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), userdata);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sender.sendMessage(config.getPluginPrefix() + ChatColor.GRAY + "Added " + amount + " minutes playtime to " + target.getName());
        }
    }

    public void fileRemoveTime(Player sender, OfflinePlayer target, int amount) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UserData userdata = null;
        try {
            userdata = mapper.readValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (target.isOnline()){
            filePlayerQuit(target.getUniqueId());
            if (userdata.getPlayTime() < amount) {
                sender.sendMessage(config.getAmountHigherThanPlayTimeError());
                filePlayerJoined((Player) target);
            } else {
                userdata.setPlayTime(userdata.getPlayTime() - (amount * 60));
                try {
                    mapper.writeValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filePlayerJoined((Player) target);
                sender.sendMessage("");
                sender.sendMessage(config.getPluginPrefix() + ChatColor.GRAY + "Removed " + amount + " minutes playtime from " + target.getName());
            }
        } else {
            if (userdata.getPlayTime() < amount) {
                sender.sendMessage(config.getAmountHigherThanPlayTimeError());
                filePlayerJoined((Player) target);
            } else {
                userdata.setPlayTime(userdata.getPlayTime() - (amount * 60));
                try {
                    mapper.writeValue(new File(BASEPATHFILE + target.getUniqueId() + ".yml"), userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sender.sendMessage("");
                sender.sendMessage(config.getPluginPrefix() + ChatColor.GRAY + "Removed " + amount + " minutes playtime from " + target.getName());
            }
        }
    }
    
    public void fileSaveAllPlayers(){
        for (UUID uuid:cache.keySet()) {
            filePlayerQuit(uuid);
        }
        
    }

    private String getJoinedDate(Player player){
        Date joinedDate = new Date(player.getFirstPlayed());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return DATE_FORMAT.format(joinedDate);
    }
}