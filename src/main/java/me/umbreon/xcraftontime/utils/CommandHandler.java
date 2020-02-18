package me.umbreon.xcraftontime.utils;

import me.umbreon.xcraftontime.Ontime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private Ontime main;

    public CommandHandler(Ontime ontime) {
        main = ontime;
    }

    @Deprecated
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
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        }
                        if (amount <= 0) {
                            sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getNotAnIntegerError());
                        } else {
                            main.databaseHandler.AddTime((Player) sender, target, amount);
                        }
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getCommandIsMissingArgsError() + "\n" + main.configHandler.ontimeAddUsage());
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getCommandIsMissingArgsError() + "\n" + main.configHandler.ontimeAddUsage());
                }
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
                        sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getCommandIsMissingArgsError() + "\n" + main.configHandler.ontimeRemoveUsage());
                    }
                } else {
                    sender.sendMessage(main.configHandler.getPluginPrefix() + main.configHandler.getCommandIsMissingArgsError() + "\n" + main.configHandler.ontimeRemoveUsage());
                }
            } else if (args[0].equalsIgnoreCase("top") && sender.hasPermission("xcraftontime.top")) {
                main.databaseHandler.topTen((Player) sender);
            }else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info") && sender.hasPermission("xcraftontime.help")){
                sender.sendMessage(main.configHandler.getPluginPrefix() + " ArdaniaOntimeTracker Hilfeseite:");
                if (sender.hasPermission("xcraftontime.check")){
                    sender.sendMessage("/ontime - Zeit dir die Onlinezeit von dir an.");
                }
                if (sender.hasPermission("xcraftontime.check.others")){
                    sender.sendMessage("/ontime [Spieler] - Zeit dir die Onlinezeit von dem Spieler an.");
                }
                if (sender.hasPermission("xcraftontime.top")){
                    sender.sendMessage("/ontime [top] - Zeit dir die Onlinezeit von den 10 besten Spielern an.");
                }
                if (sender.hasPermission("xcraftontime.add")){
                    sender.sendMessage("/ontime add [Spieler] [Zeit in Sekunden] - Fügt die angegebene Zeit dem Spieler hinzu.");
                }
                if (sender.hasPermission("xcraftontime.remove")){
                    sender.sendMessage("/ontime remove [Spieler] [Zeit in Sekunden] - Entfernt die angegebene Zeit dem Spieler.");
                }
                if (sender.hasPermission("xcraftontime.removeplayer")){
                    sender.sendMessage("/ontime player remove [Spieler] - Entfernt die gespeicherten Daten von dem Angebenen Spieler. " + ChatColor.DARK_RED + "Achtung: Das kann nicht wieder rückgängig gemacht werden!");
                }
            } else {
                if (sender.hasPermission("xcraftontime.check.others")){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.hasPlayedBefore()) {
                        main.databaseHandler.checkTarget((Player) sender, target);
                    } else {
                        sender.sendMessage(main.configHandler.getPluginPrefix() +  " " + main.configHandler.getPlayerNotFoundError());
                    }
                }
            }
        } else {
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