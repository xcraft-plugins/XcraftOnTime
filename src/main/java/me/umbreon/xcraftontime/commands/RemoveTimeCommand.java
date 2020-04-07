package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class RemoveTimeCommand {

    private final Logger logger;
    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;

    public RemoveTimeCommand(
        Logger logger,
        ConfigHandler configHandler,
        DatabaseHandler databaseHandler
    ) {
        this.logger = logger;
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
    }

    public void executeRemoveTimeCommand(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("xcraftontime.remove")) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.NoPermissionError());
            return;
        }

        if (!(args.length > 2)) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.CommandIsMissingArgsError());
            return;
        }


        boolean dayBool = false;
        boolean hoursBool = false;
        boolean minBool = false;
        boolean secBool = false;
        long time = 0;
        String executeError = ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.ExecuteError();


        for (int i = 2; i < args.length; i++) {
            if (args[i].length() < 2  || args.length > 6){
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

        try {
            if (time <= 0) {
                throw new NumberFormatException();
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                int databaseTime = databaseHandler.getPlaytime(offlinePlayer.getUniqueId());
                if (time <= databaseTime) {
                    long newTime = databaseTime - time;
                    if (databaseHandler.updatePlayerOnlineTime(offlinePlayer.getUniqueId(), newTime)) {
                        String unit;
                        if (newTime > 1) {
                            unit = configHandler.secondsString();
                        } else {
                            unit = configHandler.secondString();
                        }

                        commandSender.sendMessage(String.format(configHandler.RemoveTimeMessage(), time, unit, offlinePlayer.getName()));

                        if (configHandler.isPluginDebugging()) {
                            logger.info(
                                String.format(configHandler.RemoveTime(), offlinePlayer, time, unit, commandSender)
                            );
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + configHandler.ExecuteError());
                    }
                } else {
                    commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + configHandler.AmountHigherThanPlaytimeError());
                }
            }
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + configHandler.NotAnIntegerError());
        }
    }
}
