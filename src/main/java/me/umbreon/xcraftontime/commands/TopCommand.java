package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.data.PlayertimeRecord;
import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class TopCommand {

    private final ConfigHandler configHandler;
    private final DatabaseHandler databaseHandler;

    public TopCommand(ConfigHandler configHandler, DatabaseHandler databaseHandler) {
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
    }

    public void showTopList(CommandSender commandSender) {
        if (commandSender.hasPermission("xcraftontime.top")) {
            commandSender.sendMessage(generateTopListMessage());
        } else {
            commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.PlayerNotFoundError());
        }
    }

    private String generateTopListMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        int topListMax = configHandler.getTopListMax();
        int amount = Bukkit.getServer().getOperators().size() + topListMax;
        LinkedList<PlayertimeRecord> listOfPlayerTimeRecord = databaseHandler.getTopPlayerTimes(amount);
        stringBuilder
                .append(ChatColor.WHITE)
                .append("[")
                .append(ChatColor.RED)
                .append(configHandler.pluginPrefixString())
                .append(ChatColor.WHITE).append("] ")
                .append(configHandler.statisticsString())
                .append(" - Top ")
                .append(topListMax).append(" ")
                .append(configHandler.playerString());
        LinkedList<PlayertimeRecord> listOfPlayerWithoutOperator = listOfPlayerTimeRecord.stream().filter(this::isValid).collect(Collectors.toCollection(LinkedList::new));
        int x = Math.min(listOfPlayerWithoutOperator.size(), configHandler.getTopListMax());
        for (int i = 0; i < x; i++) {
            buildMessage(i, listOfPlayerWithoutOperator.get(i), stringBuilder);
        }

        return stringBuilder.toString();
    }

    private boolean isValid(PlayertimeRecord playertimeRecord) {
        return !Bukkit.getOfflinePlayer(playertimeRecord.getUuid()).isOp();
    }

    private void buildMessage(int index, PlayertimeRecord playertimeRecord, StringBuilder stringBuilder) {
        stringBuilder
                .append("\n")
                .append(ChatColor.AQUA)
                .append(String.format("%2s", index + 1)).append(": ").append(ChatColor.GOLD)
                .append(playertimeRecord.getPlayername()).append(" ").append(ChatColor.AQUA)
                .append(TimeConverter.secondsToDays(playertimeRecord.getPlaytime())).append(" ")
                .append(configHandler.daysString()).append(" ")
                .append(TimeConverter.secondsToHours(playertimeRecord.getPlaytime())).append(" ")
                .append(configHandler.hoursString()).append(" ")
                .append(TimeConverter.secondsToMinutes(playertimeRecord.getPlaytime())).append(" ")
                .append(configHandler.minutesString());
    }
}
