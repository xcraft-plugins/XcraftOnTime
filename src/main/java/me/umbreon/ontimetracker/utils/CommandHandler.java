package me.umbreon.ontimetracker.utils;

import me.umbreon.ontimetracker.OntimeTracker;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private OntimeTracker main;

    public CommandHandler(OntimeTracker ontimeTracker) {
        main = ontimeTracker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("add") && sender.hasPermission("xcraftontime.add")) {
                int amount = 0;
                if (args.length > 1){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (args.length > 2){
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() +""+ main.configHandler.getNotAnIntegerError());
                        }
                        if (amount <= 0) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() +""+ main.configHandler.getNotAnIntegerError());
                        } else {
                            main.databaseHandler.sqlAddTime((Player) sender, target, amount);
                        }
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getCommandIsMissingArgsError() + "\nUsage: /ontime add [Player] [Time in minutes]");
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() +""+ main.configHandler.getCommandIsMissingArgsError() + "\nUsage: /ontime add [Player] [Time in minutes]");
                }
            } else if (args[0].equalsIgnoreCase("remove") && sender.hasPermission("xcraftontime.remove")) {
                int amount = 0;
                if (args[1] != null) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (args[2] != null) {
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        }
                        if (amount <= 0) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        } else {
                            main.databaseHandler.sqlRemoveTime((Player) sender, target, amount);
                        }
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                }
            } else if (args[0].equalsIgnoreCase("top") && sender.hasPermission("xcraftontime.top")){
                main.databaseHandler.sqlGetTopTen((Player) sender);
            } else {
                if (sender.hasPermission("xcraftontime.check.others")){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.hasPlayedBefore()) {
                        main.databaseHandler.sqlCheckTarget((Player) sender, target);
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                    }
                }
            }
        } else {
            if (sender.hasPermission("xcraftontime.check")) {
                main.databaseHandler.sqlCheckPlayer((Player) sender);
            }
        }
        return false;
    }
}