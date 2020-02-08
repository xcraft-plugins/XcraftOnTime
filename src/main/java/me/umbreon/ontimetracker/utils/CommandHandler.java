package me.umbreon.ontimetracker.utils;

import me.umbreon.ontimetracker.OntimeTracker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private OntimeTracker main;

    public CommandHandler(OntimeTracker ontimeTracker) {
        main = ontimeTracker;
    }

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0) {
            //Command for /ontime add
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
                            main.databaseHandler.AddTime((Player) sender, target, amount);
                        }
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getCommandIsMissingArgsError() + "\nUsage: /ontime add [Player] [Time in minutes]");
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() +""+ main.configHandler.getCommandIsMissingArgsError() + "\nUsage: /ontime add [Player] [Time in minutes]");
                }

            //Command for /ontime remove
            } else if (args[0].equalsIgnoreCase("remove") && sender.hasPermission("xcraftontime.remove")) {
                int amount = 0;
                if (args.length > 1) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (args.length > 2) {
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        }
                        if (amount <= 0) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        } else {
                            main.databaseHandler.RemoveTime((Player) sender, target, amount);
                        }
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                }

            //Command for /ontime top
            } else if (args[0].equalsIgnoreCase("top") && sender.hasPermission("xcraftontime.top")){
                main.databaseHandler.topTen((Player) sender);
            } else {
                //Command for /ontime [PlayerName]
                if (sender.hasPermission("xcraftontime.check.others")){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.hasPlayedBefore()) {
                        main.databaseHandler.checkTarget((Player) sender, target);
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getPlayerNotFoundError());
                    }
                }
            }
        } else {
            //Command for /ontime
            if (sender.hasPermission("xcraftontime.check")) {
                main.databaseHandler.checkPlayer((Player) sender);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("xcraftontime.add")) completions.add("add");
                if (player.hasPermission("xcraftontime.remove")) completions.add("remove");
                if (player.hasPermission("xcraftontime.top")) completions.add("top");
                if (player.hasPermission("xcraftontime.check")) completions.add(player.getName());
                if (player.hasPermission("xcraftontime.check.others")){
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        completions.add(p.getName());
                    }
                }
            } else if (args.length == 2 && args[0].equals("add") && player.hasPermission("xcraftontime.add")){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            } else if (args.length == 2 && args[0].equals("remove") && player.hasPermission("xcraftontime.remove")){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }
        }
        return completions;
    }
}