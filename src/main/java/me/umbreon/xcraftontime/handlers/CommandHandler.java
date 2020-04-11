package me.umbreon.xcraftontime.handlers;

import me.umbreon.xcraftontime.TimeTracker;
import me.umbreon.xcraftontime.XcraftOnTimePlugin;
import me.umbreon.xcraftontime.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class CommandHandler extends TabCompleterHandler implements CommandExecutor {

    private final CheckCommand checkCommand;
    private final AddTimeCommand addTimeCommand;
    private final CheckOthersCommand checkOthersCommand;
    private final HelpCommand helpCommand;
    private final RemoveTimeCommand removeTimeCommand;
    private final TopCommand topCommand;
    private final ClearCommand deletePlayerCommand;
    private final ReloadCommand reloadCommand;

    private final ConfigHandler configHandler;

    public CommandHandler(
        Logger logger,
        DatabaseHandler databaseHandler,
        ConfigHandler configHandler,
        TimeTracker timeTracker,
        XcraftOnTimePlugin onlineTimeTracker
    ) {
        this.configHandler = configHandler;

        addTimeCommand = new AddTimeCommand(logger, configHandler, databaseHandler);
        checkCommand = new CheckCommand(configHandler, databaseHandler, timeTracker);
        checkOthersCommand = new CheckOthersCommand(timeTracker, configHandler, databaseHandler);
        helpCommand = new HelpCommand(configHandler);
        removeTimeCommand = new RemoveTimeCommand(logger, configHandler, databaseHandler);
        topCommand = new TopCommand(configHandler, databaseHandler);
        deletePlayerCommand = new ClearCommand(logger, configHandler, databaseHandler);
        reloadCommand = new ReloadCommand(logger, onlineTimeTracker, configHandler);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0) {
            handleArgsAvailable(sender, args);
        } else {
            handleNoArgsAvailable(sender);
        }
        return false;
    }

    private void handleNoArgsAvailable(CommandSender sender) {
        if (sender.hasPermission("xcraftontime.check")) {
            checkCommand.checkPlayerCommand((Player) sender);
        }
    }

    private void handleArgsAvailable(CommandSender sender, String[] args) {
        switch (args[0].toLowerCase()) {
            case "add":
                addTimeCommand.executeAddTimeCommand(sender, args);
                break;
            case "remove":
                removeTimeCommand.executeRemoveTimeCommand(sender, args);
                break;
            case "top":
                topCommand.showTopList(sender);
                break;
            case "help":
            case "info":
                helpCommand.showHelpPage(sender);
                break;
            case "clear":
                deletePlayerCommand.executeClearCommand(sender, args);
                break;
            case "reload":
                reloadCommand.reloadConfig((Player) sender);
                break;
            default:
                if (sender.hasPermission("xcraftontime.check.others")) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.hasPlayedBefore()) {
                        checkOthersCommand.checkOther((Player) sender, target);
                    } else {
                        sender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.PlayerNotFoundError());
                    }
                }
                break;
        }
    }
}
