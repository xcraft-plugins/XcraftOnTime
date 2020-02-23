package me.umbreon.xcraftontime.converter;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.Bukkit;

import java.sql.*;

public class Converter {

    private OnlineTimeTracker main;
    private ConfigHandler config;

    private Connection connection;

    public Converter(OnlineTimeTracker ontime, ConfigHandler configHandler) {
        this.main = ontime;
        this.config = configHandler;
    }

    private Connection connect() {
        String host = config.getHostAdress();
        String port = config.getPort();
        String user = config.getUsername();
        String password = config.getPassword();

        String database = config.olddatabase();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Connection connection = DriverManager.getConnection(url, user, password);

            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info(e.toString());
            return null;
        }
    }

    private boolean checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = connect();

                if (connection == null || connection.isClosed()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void convert(){
        String table = config.oldtable();
        if (config.convert()){
            if (checkConnection()){
                String entry = "SELECT playtime, uuid FROM " + table + " ORDER BY id DESC";
                try {
                    PreparedStatement statement = null;
                    statement = connection.prepareStatement(entry);
                    ResultSet rs = null;
                    rs = statement.executeQuery();
                    while (rs.next()){
                        int playtime = rs.getInt("playtime") / 1000;
                        String uuid = rs.getString("uuid");
                        newEntry(uuid, playtime);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void newEntry(String uuid, int playtime){
        String table = config.oldtable();
        String entry = "INSERT INTO " + table + "(uuid, playtime) VALUES (?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(entry);
            statement.setString(1, uuid);
            statement.setInt(2, playtime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
