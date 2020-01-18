package me.umbreon.ontimetracker.utils;

import me.umbreon.ontimetracker.OntimeTracker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DatabaseHandler {

    private OntimeTracker main;
    private HashMap<UUID, java.util.Date> cache = new HashMap<UUID, Date>();
    private String table = "ontimetracker_players";
    public String sqliteUrl;
    private Connection connection;
    private ConfigHandler config;

    public DatabaseHandler(OntimeTracker ontimeTracker, ConfigHandler configHandler) {
        this.main = ontimeTracker;
        setSqliteUrl();
        this.config = configHandler;
    }

    private void setSqliteUrl() {
        this.sqliteUrl = "jdbc:sqlite:" + main.getDataFolder().getAbsolutePath() + "\\OntimeTracker.db";
    }

    public void sqlCreateConnection(){
        try {
            connection = DriverManager.getConnection(sqliteUrl);
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    public void sqlCreateTable() {

        String createTable = "CREATE TABLE IF NOT EXISTS " + table + " (\n"
                + "    uuid text PRIMARY KEY,\n"
                + "    playtime integer NOT NULL,\n"
                + "    joinDate text NOT NULL\n"
                + ");";
        try {
            connection.createStatement().execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewPlayerEntry(Player player){
        String entry = "INSERT INTO " + table + "(uuid, playtime, joindate) VALUES (?,?,?)";

        try {
            PreparedStatement statement = null;
            statement = connection.prepareStatement(entry);
            statement.setString(1, String.valueOf(player.getUniqueId()));
            statement.setInt(2, 0);
            statement.setString(3, getJoinedDate(player));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sqlPlayerJoin(Player player){
        String select = "SELECT uuid FROM " + table + " WHERE uuid = ?";
        try {
            PreparedStatement statement = null;
            statement = connection.prepareStatement(select);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();
            if (!rs.next()){
                createNewPlayerEntry(player);
                cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
            } else {
                cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sqlPlayerQuit(UUID uuid) {
        String update = "UPDATE " + table + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(update);
            int playedTime = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(uuid).getTime(), TimeUnit.MILLISECONDS));
            statement.setInt(1, sqlGetPlayTime(uuid) + playedTime);
            statement.setString(2, uuid.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int sqlGetPlayTime(UUID uuid){
        int playtime = 0;
        String dbGetPlaytime = "SELECT playtime FROM " + table + " WHERE UUID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(dbGetPlaytime);
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                playtime = rs.getInt("playtime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playtime;
    }

    public void sqlCheckPlayer(Player sender){
        UUID uuid = sender.getUniqueId();
        int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS) + sqlGetPlayTime(uuid));
        long TotalDays = time / (24 * 3600);
        time = time % (24 * 3600);
        long TotalHours = time / 3600;
        time %= 3600;
        long TotalMins = time / 60 ;
        sender.sendMessage(config.getPluginPrefix() + config.getPlayerName() + sender.getDisplayName());
        sender.sendMessage(config.getPluginPrefix() + config.getPlayTime() + TotalDays + config.getDaysValue() + TotalHours + config.getHoursValue() + TotalMins + config.getMinutesValue());
        if (sender.hasPermission("ontimetracker.see.mydate")) {
            sender.sendMessage(config.getPluginPrefix() + config.getJoinDate() + sqlGetJoinedDate(uuid));
        }
    }

    public void sqlCheckTarget(Player sender, OfflinePlayer target){
        UUID uuid = target.getUniqueId();
        if (target.isOnline()){
            int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS) + sqlGetPlayTime(uuid));
            long TotalDays = time / (24 * 3600);
            time = time % (24 * 3600);
            long TotalHours = time / 3600;
            time %= 3600;
            long TotalMins = time / 60 ;
            sender.sendMessage(config.getPluginPrefix() + config.getPlayerName() + target.getName());
            sender.sendMessage(config.getPluginPrefix() + config.getPlayTime() + TotalDays + config.getDaysValue() + TotalHours + config.getHoursValue() + TotalMins + config.getMinutesValue());
            if (sender.hasPermission("ontimetracker.see.othersdate")) {
                sender.sendMessage(config.getPluginPrefix() + config.getJoinDate() + sqlGetJoinedDate(uuid));
            }
        } else {
            long time = sqlGetPlayTime(uuid);
            long TotalDays = time / (24 * 3600);
            time = time % (24 * 3600);
            long TotalHours = time / 3600;
            time %= 3600;
            long TotalMins = time / 60 ;
            sender.sendMessage(config.getPluginPrefix() + config.getPlayerName() + target.getName());
            sender.sendMessage(config.getPluginPrefix() + config.getPlayTime() + TotalDays + config.getDaysValue() + TotalHours + config.getHoursValue() + TotalMins + config.getMinutesValue());
            if (sender.hasPermission("ontimetracker.see.othersdate")) {
                sender.sendMessage(config.getPluginPrefix() + config.getJoinDate() + sqlGetJoinedDate(uuid));
            }
        }
    }

    public void sqlAddTime(Player sender, OfflinePlayer target, int amount){
        String update = "UPDATE " + table + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(update);
            statement.setInt(1, amount);
            statement.setString(2, target.getUniqueId().toString());
            sender.sendMessage(config.getPluginPrefix() + "Added " + amount + " to Seconds" + target);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getJoinedDate(Player player){
        Date joinedDate = new Date(player.getFirstPlayed());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return DATE_FORMAT.format(joinedDate);
    }

    public String sqlGetJoinedDate(UUID uuid){
        String joindate = null;
        String select = "SELECT joinDate FROM " + table + " WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(select);
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                joindate = rs.getString("joinDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joindate;
    }

    public void sqlSaveAll(){
        //Todo: What when the server crashes? The playtime shouldn't be lost
        for (UUID uuid:cache.keySet()) {
            sqlPlayerQuit(uuid);
        }
        Bukkit.getLogger().info("[OntimeTracker] Saved all players.");
    }
}
