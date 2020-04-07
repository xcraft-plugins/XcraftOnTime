package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand {

    private final ConfigHandler configHandler;

    public HelpCommand(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public void showHelpPage(CommandSender commandSender) {
        if (!commandSender.hasPermission("xcraftontime.help")) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.NoPermissionError());
            return;
        }

        commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " OntimeTracker " + configHandler.helpPageString() + ":");

        String verticalBar = ChatColor.RESET + "| " + ChatColor.AQUA;
        String verticalBar1 = ChatColor.RESET + "| " + ChatColor.BLUE;

        if (commandSender.hasPermission("xcraftontime.check")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime \n" + verticalBar1 + configHandler.CheckCommandHelpMessage());
        }

        if (commandSender.hasPermission("xcraftontime.check.others")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime [" + configHandler.playerString() + "] \n" + verticalBar1 + configHandler.CheckOthersCommandHelpMessage());
        }

        if (commandSender.hasPermission("xcraftontime.top")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime [top]\n" + verticalBar1 + configHandler.TopCommandHelpMessage());
        }

        if (commandSender.hasPermission("xcraftontime.add")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime add [" + configHandler.playerString() + "] [" + configHandler.timeString() + "]\n" + verticalBar1 + configHandler.AddTimeCommandHelpMessage());
        }

        if (commandSender.hasPermission("xcraftontime.remove")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime remove [" + configHandler.playerString() + "] [" + configHandler.timeString() + "]\n" + verticalBar1 + configHandler.RemoveTimeCommandHelpMessage());
        }

        if (commandSender.hasPermission("xcraftontime.reload")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime reload\n" + verticalBar1 + configHandler.ReloadHelpMessage()
            );
        }

        if (commandSender.hasPermission("xcraftontime.clear")) {
            commandSender.sendMessage(
                    verticalBar + "/ontime clear [" + configHandler.playerString() + "]\n" +
                            verticalBar1 + configHandler.ClearCommandHelpMessage() + "\n" +
                            verticalBar + ChatColor.DARK_RED + configHandler.NoUndoWarning());
        }

        commandSender.sendMessage(ChatColor.GRAY + "Plugin Version 1.0.0 created by Umbreon");
    }
}
