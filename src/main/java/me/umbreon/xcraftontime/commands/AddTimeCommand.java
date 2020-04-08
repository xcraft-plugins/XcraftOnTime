package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class AddTimeCommand {

    private final Logger logger;
    private final ConfigHandler configHandler;
    private final DatabaseHandler databaseHandler;

    public AddTimeCommand(
        Logger logger,
        ConfigHandler configHandler,
        DatabaseHandler databaseHandler
    ) {
        this.logger = logger;
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
    }

    public void executeAddTimeCommand(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("xcraftontime.add")) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.NoPermissionError());
            return;
        }

        if (!(args.length > 2)) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.CommandIsMissingArgsError());
            return;
        }

        String player = databaseHandler.getPlayerName(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
        if (player == null) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.PlayerNotFoundError());
            return;
        }

        boolean dayBool = false;
        boolean hoursBool = false;
        boolean minBool = false;
        boolean secBool = false;
        long time = 0;
        String executeError = ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.ExecuteError();


        for (int i = 2; i < args.length; i++) {
            if (args[i].length() < 2 || args.length > 6) {
                return;
            }
            switch (args[i].substring(args[i].length() - 1)) {
                case "d":
                    if (!dayBool) {
                        time = time + TimeConverter.daysToSeconds(Integer.parseInt(args[i].substring(0, args[i].length() - 1)));
                        dayBool = true;
                    } else {
                        commandSender.sendMessage(executeError);
                        return;
                    }
                    break;
                case "h":
                    if (!hoursBool) {
                        time = time + TimeConverter.hoursToSeconds(Integer.parseInt(args[i].substring(0, args[i].length() - 1)));
                        hoursBool = true;
                    } else {
                        commandSender.sendMessage(executeError);
                        return;
                    }
                    break;
                case "m":
                    if (!minBool) {
                        time = time + TimeConverter.minutesToSeconds(Integer.parseInt(args[i].substring(0, args[i].length() - 1)));
                        minBool = true;
                    } else {
                        commandSender.sendMessage(executeError);
                        return;
                    }
                    break;
                case "s":
                    if (!secBool) {
                        time = time + Integer.parseInt(args[i].substring(0, args[i].length() - 1));
                        secBool = true;
                    } else {
                        commandSender.sendMessage(executeError);
                        return;
                    }
                    break;
                default:
                    commandSender.sendMessage(executeError);
                    break;
            }
        }

        logger.info(time + "");

        try {
            if (time <= 0) {
                throw new NumberFormatException();
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                databaseHandler.updatePlayerOnlineTime(offlinePlayer.getUniqueId(), databaseHandler.getPlaytime(offlinePlayer.getUniqueId()) + time);
                String unit;

                if (time > 1) {
                    unit = configHandler.secondsString();
                } else {
                    unit = configHandler.secondString();
                }

                commandSender.sendMessage(String.format(configHandler.AddTimeMessage(), time, unit, offlinePlayer.getName()));

                if (configHandler.isPluginDebugging()) {
                    logger.info(
                        String.format(configHandler.AddTime(), offlinePlayer.getName(), time, unit, commandSender.getName())
                    );
                }
            }
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.NotAnIntegerError());
        }
    }
}
