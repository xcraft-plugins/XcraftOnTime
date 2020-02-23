package me.umbreon.xcraftontime.handlers;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.events.PlayerJoin;
import me.umbreon.xcraftontime.events.PlayerQuit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseHandler {

    private OnlineTimeTracker main;
    private ConfigHandler config;
    public Map<UUID, Date> cache = new HashMap<>();
    public Connection connection;
    private PlayerQuit PlayerQuit;
    private PlayerJoin PlayerJoin;

    public DatabaseHandler(OnlineTimeTracker onlineTimeTracker, ConfigHandler configHandler) {
        this.main = onlineTimeTracker;
        this.config = configHandler;
        PlayerQuit = new PlayerQuit(onlineTimeTracker, this);
        PlayerJoin = new PlayerJoin(onlineTimeTracker, configHandler, this);
    }

    public Connection startup() {
        String host = config.getHostAdress();
        String port = config.getPort();
        String database = config.getDatabaseName();
        String user = config.getUsername();
        String password = config.getPassword();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Connection connection = DriverManager.getConnection(url, user, password);

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + config.getTable() + " (\n"
                    + "uuid VARCHAR(50) PRIMARY KEY,\n"
                    + "playtime INT(255) NOT NULL\n"
                    + ")"
            );

            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info(e.toString());
            return null;
        }
    }

    public boolean checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = startup();

            if (connection == null || connection.isClosed()) {
                Bukkit.getLogger().info("[XCraftOntime] Couldn't connect to database! !");
                return false;
            }

        }
        return true;
    }

    public void createNewEntry(Player player, int amount){
        String entry = "INSERT INTO " + config.getTable() + "(uuid, playtime) VALUES (?,?)";

        try {
            if (checkConnection()){
                PreparedStatement statement = null;
                statement = connection.prepareStatement(entry);
                statement.setString(1, String.valueOf(player.getUniqueId()));
                statement.setInt(2, amount);
                statement.executeUpdate();
                if (config.isPluginDebugging()){
                    Bukkit.getLogger().info("[" + config.getTable() + "]" + " Created new entry for player " + player.getName());
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("[XCraftOntime] Couldn't connect to database!");
        }
    }

    public int getPlaytime(UUID uuid) {
        int playtime = 0;
        String select = "SELECT playtime FROM " + config.getTable() + " WHERE UUID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(select);
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                playtime = rs.getInt("playtime");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("[XCraftOntime] Couldn't connect to database!");
        }
        return playtime;
    }

    public void Shutdown(){
        for (UUID uuid:cache.keySet()) {
            PlayerQuit.quitEvent(uuid);
        }
        if (connection != null) {
            try {
                connection.close();
                Bukkit.getLogger().info("[XCraftOntime] Closed database connection!");
            } catch (SQLException e) {
                Bukkit.getLogger().info("[XCraftOntime] Couldn't connect to database!");
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
                    PlayerQuit.quitEvent(player.getUniqueId());
                    PlayerJoin.joinEvent(player);
                }
            }
        }, 0L, 20L * SleepTimer * 60);
        if (config.isPluginDebugging()){
            Bukkit.getLogger().info("[XcraftOntime] Saved all playtime");
        }
    }
}
