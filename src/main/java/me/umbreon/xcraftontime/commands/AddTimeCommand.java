package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.handlers.AddTimeHandler;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AddTimeCommand {

    private ConfigHandler configHandler;
    private AddTimeHandler addTimeHandler;

    public AddTimeCommand( OnlineTimeTracker onlineTimeTracker ) {
        configHandler = onlineTimeTracker.getConfigHandler();

        addTimeHandler = new AddTimeHandler(configHandler, onlineTimeTracker.getDatabaseHandler());
    }

    public void executeAddTimeCommand(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission("xcraftontime.add")) {
            if (args.length > 2) {
                try {
                    int amount = Integer.parseInt(args[2]);
                    if (amount <= 0) {
                        commandSender.sendMessage(configHandler.getPluginPrefix() + " " + configHandler.NotAnIntegerError());
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        addTimeHandler.AddTime(commandSender, target, amount );
                    }
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(configHandler.getPluginPrefix() + " " + configHandler.NotAnIntegerError());
                }
            } else {
                commandSender.sendMessage(configHandler.getPluginPrefix() + " " + configHandler.CommandIsMissingArgsError() + "\n" + configHandler.AddTimeUsage());
            }
        } else {
            commandSender.sendMessage(configHandler.getPluginPrefix() + " " + configHandler.PlayerNotFoundError());
        }
    }
}
