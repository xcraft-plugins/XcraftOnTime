package me.umbreon.xcraftontime.handlers;

import me.umbreon.xcraftontime.OnlineTimeTracker;
import me.umbreon.xcraftontime.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private OnlineTimeTracker onlineTimeTracker;

    private CheckCommand checkCommand;
    private AddTimeCommand addTimeCommand;
    private CheckOthersCommand checkOthersCommand;
    private HelpCommand helpCommand;
    private RemoveTimeCommand removeTimeCommand;
    private TopCommand topCommand;
    private MoveStatsCommand moveStatsCommand;
    private DeletePlayerCommand deletePlayerCommand;

    public CommandHandler(OnlineTimeTracker onlineTimeTracker ) {
        this.onlineTimeTracker = onlineTimeTracker;
        initCommands();
    }

    private void initCommands() {
        addTimeCommand = new AddTimeCommand(onlineTimeTracker);
        checkCommand = new CheckCommand(onlineTimeTracker);
        checkOthersCommand = new CheckOthersCommand(onlineTimeTracker);
        helpCommand = new HelpCommand(onlineTimeTracker.getConfigHandler());
        removeTimeCommand = new RemoveTimeCommand(onlineTimeTracker);
        topCommand = new TopCommand(onlineTimeTracker);
        moveStatsCommand = new MoveStatsCommand(onlineTimeTracker);
        deletePlayerCommand = new DeletePlayerCommand(onlineTimeTracker);
    }

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args ) {
        if ( args.length > 0 ) {
            handleArgsAvailable( sender, args );
        } else {
            handleNoArgsAvailable( sender );
        }
        return false;
    }

    private void handleNoArgsAvailable( CommandSender sender ) {
        if ( sender.hasPermission( "xcraftontime.check" ) ) {
            checkCommand.checkPlayerCommand( (Player) sender );
        }
    }

    private void handleArgsAvailable( CommandSender sender, String[] args ) {
        switch ( args[0].toLowerCase() ) {
            case "add":
                addTimeCommand.executeAddTimeCommand(sender, args);
                break;
            case "remove":
                removeTimeCommand.executeRemoveTimeCommand(sender, args);
                break;
            case "top":
                topCommand.showTopList((Player) sender);
                break;
            case "help":
            case "info":
                helpCommand.showHelpPage((Player) sender);
                break;
            case "move":
                moveStatsCommand.executeMoveStatsCommand(sender, args);
                break;
            case "delete":
                deletePlayerCommand.executeRemovePlayerCommand(sender, args);
                break;
            default:
                if (sender.hasPermission("xcraftontime.check.others")){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.hasPlayedBefore()) {
                        checkOthersCommand.checkOther((Player) sender, target);
                    } else {
                        sender.sendMessage(onlineTimeTracker.getConfigHandler().getPluginPrefix() +  " " + onlineTimeTracker.getConfigHandler().PlayerNotFoundError());
                    }
                }
                break;
        }
    }
}
