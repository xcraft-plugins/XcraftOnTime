package me.umbreon.ontimetracker.utils;

import me.umbreon.ontimetracker.OntimeTracker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;


public class CommandHandler implements CommandExecutor {

    private OntimeTracker main;
    private static String BASEPATHFILE = OntimeTracker.BASEPATHFILE;


    public CommandHandler(OntimeTracker ontimeTracker) {
        main = ontimeTracker;
    }

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("add")) {
                int amount = 0;
                if (args.length > 1){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (args.length > 2){
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {

                            sender.sendMessage(main.configHandler.getPluginPrefix() +""+ main.configHandler.getNotAnIntegerError());
                        }
                        File userFile = new File(BASEPATHFILE + target.getUniqueId() + ".yml");
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

            } else if (args[0].equalsIgnoreCase("remove")) {
                int amount = 0;
                if (args[1] != null){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (args[2] != null){
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        }
                        File userFile = new File(BASEPATHFILE + target.getUniqueId() + ".yml");
                        if (!userFile.exists()) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                        } else {
                            if (amount <= 0) {
                                sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                            } else {
                                //main.databaseHandler.sqlRemoveTime((Player) sender, target, amount);
                            }
                        }
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                }

            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target.hasPlayedBefore()) {
                    main.databaseHandler.sqlCheckTarget((Player) sender, target);
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                }
            }
        } else {
            main.databaseHandler.sqlCheckPlayer((Player) sender);
        }
        return false;
    }
}