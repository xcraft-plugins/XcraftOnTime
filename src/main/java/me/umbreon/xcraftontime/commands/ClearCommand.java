package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class ClearCommand {

    private final Logger logger;
    private final ConfigHandler configHandler;
    private final DatabaseHandler databaseHandler;

    public ClearCommand(
        Logger logger,
        ConfigHandler configHandler,
        DatabaseHandler databaseHandler
    ) {
        this.logger = logger;
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
    }

    public void executeClearCommand(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("xcraftontime.clear")) {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.PlayerNotFoundError());
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        databaseHandler.executeRemovePlayerCommand(offlinePlayer);
        commandSender.sendMessage(String.format(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + configHandler.ClearMessage(), offlinePlayer));

        if (configHandler.isPluginDebugging()) {
            logger.info(String.format(configHandler.Clear(), offlinePlayer, commandSender));
        }
    }
}
