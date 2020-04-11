package me.umbreon.xcraftontime.handlers;

import me.umbreon.xcraftontime.data.PlayertimeRecord;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Logger;

public class DatabaseHandler {

    private final Logger logger;
    private final ConfigHandler configHandler;
    public static Connection connection;

    private PreparedStatement getPlayernameByUuidStatement;
    private PreparedStatement updatePlayerOnlineTimeStatement;
    private PreparedStatement setNewPlayernameStatement;
    private PreparedStatement deletePlayerStatement;
    private PreparedStatement showTopListStatement;
    private PreparedStatement createNewEntryStatement;
    private PreparedStatement getPlaytimeStatement;
    private PreparedStatement addPlayerOnlineTimeStatement;
    private PreparedStatement statement;

    public DatabaseHandler(
        Logger logger,
        ConfigHandler configHandler
    ) {
        this.logger = logger;
        this.configHandler = configHandler;
    }

    public Connection startup() {
        String host = configHandler.getHostAdress();
        String port = configHandler.getPort();
        String database = configHandler.getDatabaseName();
        String user = configHandler.getUsername();
        String password = configHandler.getPassword();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Connection connection = DriverManager.getConnection(url, user, password);

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + configHandler.getTable() + " (\n"
                    + "uuid VARCHAR(50) PRIMARY KEY,\n"
                    + "playtime INT(255) NOT NULL,\n"
                    + "name VARCHAR(50) NOT NULL\n"
                    + ")"
            );

            getPlayernameByUuidStatement = connection.prepareStatement("SELECT name FROM `" + configHandler.getTable() + "` WHERE uuid = ?");
            showTopListStatement = connection.prepareStatement("SELECT playtime, uuid, name FROM " + configHandler.getTable() + " ORDER BY playtime DESC LIMIT ?");
            getPlaytimeStatement = connection.prepareStatement("SELECT playtime FROM `" + configHandler.getTable() + "` WHERE uuid = ?");
            updatePlayerOnlineTimeStatement = connection.prepareStatement("UPDATE " + configHandler.getTable() + " SET playtime = ? WHERE uuid = ?");
            addPlayerOnlineTimeStatement = connection.prepareStatement("UPDATE " + configHandler.getTable() + " SET playtime = playtime + ? WHERE uuid = ?");
            setNewPlayernameStatement = connection.prepareStatement("UPDATE " + configHandler.getTable() + " SET name = ? WHERE uuid = ?");
            deletePlayerStatement = connection.prepareStatement("DELETE FROM " + configHandler.getTable() + " WHERE uuid = ?");
            createNewEntryStatement = connection.prepareStatement("INSERT INTO " + configHandler.getTable() + "(uuid, playtime, name) VALUES (?,?,?)");
            statement = connection.prepareStatement("SELECT * FROM " + configHandler.getTable() + " WHERE uuid = ?");


            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(e.toString());
            return null;
        }
    }

    private boolean checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = startup();

                if (connection == null || connection.isClosed()) {
                    logger.info(
                        ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + configHandler.NoConnectionToSQLError()
                    );
                    return false;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createNewEntry(Player player) {
        try {
            if (checkConnection()) {
                createNewEntryStatement.setString(1, String.valueOf(player.getUniqueId()));
                createNewEntryStatement.setInt(2, 0);
                createNewEntryStatement.setString(3, player.getName());
                createNewEntryStatement.executeUpdate();
                logger.info(
                    "[" + configHandler.getTable() + "]" + " Created new entry for player " + player.getName()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPlaytime(UUID uuid) {
        int playtime = 0;
        try {
            if (checkConnection()) {
                getPlaytimeStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = getPlaytimeStatement.executeQuery()) {
                    if (resultSet.next()) {
                        playtime = resultSet.getInt("playtime");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playtime;
    }

    public String getPlayerName(UUID uuid) {
        String name = null;
        try {
            if (checkConnection()) {
                getPlayernameByUuidStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = getPlayernameByUuidStatement.executeQuery()) {
                    if (resultSet.next()) {
                        name = resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info(
                    ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + configHandler.ConnectionToSQLClosedMessage()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePlayerOnlineTime(UUID uuid, long newTime) {
        if (checkConnection()) {
            try {
                updatePlayerOnlineTimeStatement.setLong(1, newTime);
                updatePlayerOnlineTimeStatement.setString(2, uuid.toString());
                updatePlayerOnlineTimeStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                logger.info(e.toString());
            }
        }
        return false;
    }

    public void addPlayerOnlineTime(UUID uuid, long newTime) {
        if (checkConnection()) {
            try {
                addPlayerOnlineTimeStatement.setLong(1, newTime);
                addPlayerOnlineTimeStatement.setString(2, uuid.toString());
                addPlayerOnlineTimeStatement.executeUpdate();
            } catch (SQLException e) {
                logger.info(e.toString());
            }
        }
    }

    public void initPlayer(Player player) {
        String playername = getPlayerName(player.getUniqueId());
        if (playername == null) {
            createNewEntry(player);
        } else {
            checkIfPlayerHasNewName(player, playername);
        }
    }

    private void checkIfPlayerHasNewName(Player player, String oldPlayername) {
        if (checkConnection()) {
            if (!oldPlayername.equals(player.getName())) {
                try {
                    setNewPlayernameStatement.setString(1, player.getName());
                    setNewPlayernameStatement.setString(2, player.getUniqueId().toString());
                    setNewPlayernameStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void executeRemovePlayerCommand(OfflinePlayer offlinePlayer) {
        if (checkConnection()) {
            try {
                deletePlayerStatement.setString(1, offlinePlayer.getUniqueId().toString());
                deletePlayerStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public LinkedList<PlayertimeRecord> getTopPlayerTimes(int amount) {
        LinkedList<PlayertimeRecord> listOfPlayertimeRecords = new LinkedList<>();

        try {
            showTopListStatement.setInt(1, amount);
            try (ResultSet resultSet = showTopListStatement.executeQuery()) {
                while (resultSet.next()) {
                    listOfPlayertimeRecords.add(new PlayertimeRecord(
                            resultSet.getString("name"),
                            resultSet.getLong("playtime"),
                            UUID.fromString(resultSet.getString("uuid")))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listOfPlayertimeRecords;
    }

}
