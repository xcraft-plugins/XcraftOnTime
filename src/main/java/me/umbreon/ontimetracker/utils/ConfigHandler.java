package me.umbreon.ontimetracker.utils;

import me.umbreon.ontimetracker.OntimeTracker;
import me.umbreon.ontimetracker.model.UserData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConfigHandler {

    private String BASEPATHFILE = OntimeTracker.BASEPATHFILE;
    private HashMap<UUID, Date> cache = new HashMap<UUID, Date>();
    private HashMap<UUID, UserData> onlinePlayers = new HashMap<UUID, UserData>();
    Yaml yaml = new Yaml();

    public void PlayerJoined(String playerName, UUID playerID) {
        File playerData = new File(BASEPATHFILE + playerID + ".yml");

        if(!playerData.exists()){
            createPlayerFile(playerName, playerID, playerData);
        }

        try {
            loadPlayerFile(playerID, playerData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void createPlayerFile(String playerName, UUID playerID, File playerData) {
        try {
            playerData.createNewFile();
            FileWriter fw = new FileWriter(BASEPATHFILE + playerID + ".yml");
            UserData userData = new UserData();
            userData.setJoinedDate(getDate());
            userData.setPlayTime(0);
            yaml.dump(userData, fw);
            Bukkit.getLogger().info("Created new file for " + playerName); //Todo: Add Coinfig value when the user what to see this.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDate(){
        SimpleDateFormat DATE_FORMAT;
        DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = DATE_FORMAT.format(new Date()).toString();
        return date;
    }

    private void loadPlayerFile(UUID playerID ,File playerData) throws FileNotFoundException {
        InputStream inputStream2 = new FileInputStream(playerData);
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(playerID + ".yml");
        UserData userData = yaml.load(inputStream2);
        onlinePlayers.put(playerID, userData);
        cache.put(playerID, Calendar.getInstance().getTime());
    }

    public void PlayerQuit(Player player) {
        UserData userData = onlinePlayers.get(player.getUniqueId());
        userData.setPlayTime(TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(player.getUniqueId()).getTime(),TimeUnit.MILLISECONDS));
        FileWriter fw = null;
        try {
            fw = new FileWriter(BASEPATHFILE + player.getUniqueId() + ".yml");
            yaml.dump(userData, fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
