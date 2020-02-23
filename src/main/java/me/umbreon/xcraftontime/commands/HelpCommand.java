package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand {

    private ConfigHandler configHandler;

    public HelpCommand( ConfigHandler configHandler ) {
        this.configHandler = configHandler;
    }

    public void showHelpPage(CommandSender commandSender) {
        if (commandSender.hasPermission("xcraftontime.help")) {
            commandSender.sendMessage(configHandler.getPluginPrefix() + " ArdaniaOntimeTracker Hilfeseite:");

            if (commandSender.hasPermission("xcraftontime.check")){
                commandSender.sendMessage("/ontime - Zeit dir die Onlinezeit von dir an.");
            }

            if (commandSender.hasPermission("xcraftontime.check.others")){
                commandSender.sendMessage("/ontime [Spieler] - Zeit dir die Onlinezeit von dem Spieler an.");
            }

            if (commandSender.hasPermission("xcraftontime.top")){
                commandSender.sendMessage("/ontime [top] - Zeit dir die Onlinezeit von den 10 besten Spielern an.");
            }

            if (commandSender.hasPermission("xcraftontime.add")){
                commandSender.sendMessage("/ontime add [Spieler] [Zeit in Sekunden] - Fügt die angegebene Zeit dem Spieler hinzu.");
            }

            if (commandSender.hasPermission("xcraftontime.remove")){
                commandSender.sendMessage("/ontime remove [Spieler] [Zeit in Sekunden] - Entfernt die angegebene Zeit dem Spieler.");
            }

            if (commandSender.hasPermission("xcraftontime.removeplayer")){
                commandSender.sendMessage("/ontime player remove [Spieler] - Entfernt die gespeicherten Daten von dem Angebenen Spieler. " + ChatColor.DARK_RED + "Achtung: Das kann nicht wieder rückgängig gemacht werden!");
            }
        } else {
            commandSender.sendMessage(configHandler.getPluginPrefix() + configHandler.PlayerNotFoundError());
        }
    }
}
