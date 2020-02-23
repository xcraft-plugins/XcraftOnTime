package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.RemoveTimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class RemoveTimeCommand {

    private ConfigHandler configHandler;
    private RemoveTimeHandler removeTimeHandler;

    public RemoveTimeCommand( OnlineTimeTracker onlineTimeTracker ) {
        configHandler = onlineTimeTracker.getConfigHandler();

        removeTimeHandler = new RemoveTimeHandler(configHandler, onlineTimeTracker.getDatabaseHandler());
    }

    public void executeRemoveTimeCommand(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission("xcraftontime.remove")) {
            if (args.length > 1) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (args.length > 2) {
                    try {
                        int amount = Integer.parseInt(args[2]);

                        if (amount <= 0) {
                            commandSender.sendMessage(configHandler.getPluginPrefix() + " " + configHandler.NotAnIntegerError());
                        } else {
                            removeTimeHandler.RemoveTime(commandSender, target, amount);
                        }
                    } catch (NumberFormatException e) {
                        commandSender.sendMessage(configHandler.getPluginPrefix() + " " + configHandler.NotAnIntegerError());
                    }
                } else {
                    commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.CommandIsMissingArgsError() + "\n" + configHandler.RemoveTimeUsage());
                }
            } else {
                commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.CommandIsMissingArgsError() + "\n" + configHandler.RemoveTimeUsage());
            }
        } else {
            commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.PlayerNotFoundError());
        }
    }
}
