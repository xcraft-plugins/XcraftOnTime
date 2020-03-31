package me.umbreon.xcraftontime.handlers;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler extends TabCompleterHandler implements CommandExecutor {

    private CheckCommand checkCommand;
    private AddTimeCommand addTimeCommand;
    private CheckOthersCommand checkOthersCommand;
    private HelpCommand helpCommand;
    private RemoveTimeCommand removeTimeCommand;
    private TopCommand topCommand;
    private ClearCommand deletePlayerCommand;
    private DatabaseHandler databaseHandler;
    private ConfigHandler configHandler;
    private OnlineTimeTracker onlineTimeTracker;
    private TimeHandler timeHandler;
    private ReloadCommand reloadCommand;

    public CommandHandler(DatabaseHandler databaseHandler, ConfigHandler configHandler, TimeHandler timeHandler, OnlineTimeTracker onlineTimeTracker) {
        this.databaseHandler = databaseHandler;
        this.configHandler = configHandler;
        this.timeHandler = timeHandler;
        this.onlineTimeTracker = onlineTimeTracker;
        initCommands();
    }

    private void initCommands() {
        addTimeCommand = new AddTimeCommand(configHandler, databaseHandler);
        checkCommand = new CheckCommand(configHandler, databaseHandler, timeHandler);
        checkOthersCommand = new CheckOthersCommand(timeHandler, configHandler, databaseHandler);
        helpCommand = new HelpCommand(configHandler);
        removeTimeCommand = new RemoveTimeCommand(configHandler, databaseHandler);
        topCommand = new TopCommand(configHandler, databaseHandler);
        deletePlayerCommand = new ClearCommand(configHandler, databaseHandler);
        reloadCommand = new ReloadCommand(onlineTimeTracker, configHandler);
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
