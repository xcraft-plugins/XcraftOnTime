package me.umbreon.xcraftontime.utils;

import me.umbreon.xcraftontime.Ontime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private Ontime main;
    private ConfigHandler config;
    private HashMap<UUID, java.util.Date> cache = new HashMap<UUID, Date>();
    private Connection connection;
    private String table = "Ontime";


    public DatabaseHandler(Ontime ontime, ConfigHandler configHandler) {
        this.main = ontime;
        this.config = configHandler;
    }

    public Connection startup() {
        String host = config.host();
        String port = config.port();
        String database = config.database();
        String user = config.user();
        String password = config.password();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Connection connection = DriverManager.getConnection(url, user, password);

            if (config.getDebugValue()){
                Bukkit.getLogger().info( "[" + table + "] " + "Successfully connected to database.");
            }

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + table + " (\n"
                    + "uuid VARCHAR(50) PRIMARY KEY,\n"
                    + "playtime INT(255) NOT NULL,\n"
                    + "joined VARCHAR(30) NOT NULL\n"
                    + ")"
            );

            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info(e.toString());
            return null;
        }
    }

    private boolean checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = startup();

            if (connection == null || connection.isClosed()) {
                return false;
            }
        }
        return true;
    }

    private void createNewEntry(Player player){
        String entry = "INSERT INTO " + table + "(uuid, playtime, joined) VALUES (?,?,?)";

        try {
            if (checkConnection()){
                PreparedStatement statement = null;
                statement = connection.prepareStatement(entry);
                statement.setString(1, String.valueOf(player.getUniqueId()));
                statement.setInt(2, 0);
                statement.setString(3, createDate(player));
                statement.executeUpdate();
                if (debug()){
                    Bukkit.getLogger().info("[" + table + "]" + " Created new entry for player " + player.getName());
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.toString());
        }
    }

    public void MySqlJoinEvent(Player player){
        String select = "SELECT uuid FROM " + table + " WHERE uuid = ?";
        try {
            if (checkConnection()){
                PreparedStatement statement = null;
                statement = connection.prepareStatement(select);
                statement.setString(1, player.getUniqueId().toString());
                ResultSet rs = statement.executeQuery();
                if (!rs.next()){
                    createNewEntry(player);
                    cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
                } else {
                    cache.put(player.getUniqueId(), Calendar.getInstance().getTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void MySqlQuitEvent(UUID uuid) {
        String update = "UPDATE " + table + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            if (checkConnection()){
                statement = connection.prepareStatement(update);
                int playedTime;
                if (cache.get(uuid) == null){
                    playedTime = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime(), TimeUnit.MILLISECONDS));
                } else {
                    playedTime = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(uuid).getTime(), TimeUnit.MILLISECONDS));
                }
                statement.setInt(1, getPlaytime(uuid) + playedTime);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getPlaytime(UUID uuid){
        int playtime = 0;
        String select = "SELECT playtime FROM " + table + " WHERE UUID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(select);
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

    private String createDate(Player player){
        Date joined = new Date(player.getFirstPlayed());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return DATE_FORMAT.format(joined);
    }

    private String sqlGetJoinedDate(UUID uuid){
        String joindate = null;
        String select = "SELECT joined FROM " + table + " WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(select);
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                joindate = rs.getString("joined");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joindate;
    }

    public void checkPlayer(Player sender){
        UUID uuid = sender.getUniqueId();
        int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS) + getPlaytime(uuid));
        long TotalDays = time / (24 * 3600);
        time = time % (24 * 3600);
        long TotalHours = time / 3600;
        time %= 3600;
        long TotalMins = time / 60 ;
        sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayerName() + ChatColor.RESET + " " + sender.getDisplayName());
        sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayTime() + " " + TotalDays + " " + config.getDaysValue() + " " + TotalHours + " " + config.getHoursValue() + " " + TotalMins + " " + config.getMinutesValue());
        if (sender.hasPermission("ontimetracker.see.mydate")) {
            sender.sendMessage(config.getPluginPrefix() + " " + config.getJoinDate() + " " + sqlGetJoinedDate(uuid));
        }
    }

    public void checkTarget(Player sender, OfflinePlayer target){
        UUID uuid = target.getUniqueId();
        if (target.isOnline()){
            int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() - cache.get(sender.getUniqueId()).getTime(), TimeUnit.MILLISECONDS) + getPlaytime(uuid));
            long TotalDays = time / (24 * 3600);
            time = time % (24 * 3600);
            long TotalHours = time / 3600;
            time %= 3600;
            long TotalMins = time / 60 ;
            sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayerName() + ChatColor.RESET + " " + target.getName());
            sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayTime() + " " + TotalDays + " " + config.getDaysValue() + " " + TotalHours + " " + config.getHoursValue() + " " + TotalMins + " " + config.getMinutesValue());
            if (sender.hasPermission("ontimetracker.see.othersdate")) {
                sender.sendMessage(config.getPluginPrefix() + " " + config.getJoinDate() + " " + sqlGetJoinedDate(uuid));
            }
        } else {
            long time = getPlaytime(uuid);
            long TotalDays = time / (24 * 3600);
            time = time % (24 * 3600);
            long TotalHours = time / 3600;
            time %= 3600;
            long TotalMins = time / 60 ;
            sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayerName() + ChatColor.RESET + " " + target.getName());
            sender.sendMessage(config.getPluginPrefix() + " " + config.getPlayTime() + " " + TotalDays + config.getDaysValue() + " " + TotalHours + " " + config.getHoursValue() + " " + TotalMins + " " + config.getMinutesValue());
            if (sender.hasPermission("ontimetracker.see.othersdate")) {
                sender.sendMessage(config.getPluginPrefix() + " " + config.getJoinDate() + " " + sqlGetJoinedDate(uuid));
            }
        }
    }

    public void AddTime(Player sender, OfflinePlayer target, int amount){
        String update = "UPDATE " + table + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(update);
            statement.setInt(1, amount + getPlaytime(target.getUniqueId()));
            statement.setString(2, target.getUniqueId().toString());
            statement.executeUpdate();
            if (amount>1){
                sender.sendMessage(ChatColor.DARK_GRAY + "" + amount + " Sekunden wurden dem Spieler " + target.getName() + " hinzugefügt.");
            } else {
                sender.sendMessage(ChatColor.DARK_GRAY + "" + amount + " Sekunde wurden dem Spieler " + target.getName() + " hinzugefügt.");
            }
            if (debug()){
                Bukkit.getLogger().info(table +  sender.getName() + " added " + amount + " seconds of playtime from " + target.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RemoveTime(Player sender, OfflinePlayer target, int amount){
        String update = "UPDATE " + table + " SET playtime = ? WHERE uuid = ?";
        PreparedStatement statement = null;
        try {

            if (!(amount> getPlaytime(target.getUniqueId()))){

                statement = connection.prepareStatement(update);
                statement.setInt(1, getPlaytime(target.getUniqueId()) - amount);
                statement.setString(2, target.getUniqueId().toString());
                statement.executeUpdate();

                if (amount>1){
                    sender.sendMessage(ChatColor.DARK_GRAY + "" + amount + " Sekunden wurden dem Spieler " + target.getName() + " abgezogen.");
                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "" + amount + " Sekunde wurden dem Spieler " + target.getName() + " abgezogen.");
                }

                if (debug()){
                    Bukkit.getLogger().info(table +  sender.getName() + " removed " + amount + " seconds of playtime from " + target.getName());
                }

            } else {
                sender.sendMessage(config.getPluginPrefix() + config.getAmountHigherThanPlayTimeError());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Shutdown(){
        for (UUID uuid:cache.keySet()) {
            MySqlQuitEvent(uuid);
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.toString());
            }
        }
        Bukkit.getLogger().info("[XcraftOntime] Saved all players.");
    }

    public void startTimedSaving(){
        int SleepTimer = (int) config.getSleepTimer();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()){
                    MySqlQuitEvent(player.getUniqueId());
                    MySqlJoinEvent(player);
                    if (debug()){
                        Bukkit.getLogger().info("[XcraftOntime] Saved all playtime");
                    }
                }
            }
        }, 0L, 20L*SleepTimer+1);
    }

    public void topTen(Player sender){
        String select  = "SELECT playtime, uuid FROM " + table + " ORDER BY playtime DESC";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(select);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            int stat = 1;
            int top10Max = config.getTopListMax();
            sender.sendMessage(config.getPluginPrefix() + "Spielzeitstatistik - Top " + top10Max + " " + config.getPlayerValues());
            while (rs.next()){
                if (count<top10Max){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                    String name = player.getName();
                    int time = rs.getInt("playtime");
                    long days = time / (24 * 3600);
                    time = time % (24 * 3600);
                    long hours = time / 3600;
                    time %= 3600;
                    long mins = time / 60 ;

                    if (!(stat<10)) {
                        sender.sendMessage(stat + ": " + ChatColor.GOLD + name + " " + ChatColor.AQUA + days + " " + config.getDaysValue() + " " + hours + " " + config.getHoursValue() + " " + mins + config.getMinutesValue());
                    } else {
                        sender.sendMessage("  " + stat + ": " + ChatColor.GOLD + name + " " + ChatColor.AQUA + days + " " + config.getDaysValue() + " " + hours + " " + config.getHoursValue() + " " + mins + " " + config.getMinutesValue());
                    }

                    count++;
                    stat++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean debug(){
        return config.getDebugValue;
    }

}
