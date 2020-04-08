package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import me.umbreon.xcraftontime.utils.TimeConverter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CheckOthersCommand {

    private final ConfigHandler configHandler;
    private final DatabaseHandler databaseHandler;
    private final TimeHandler timeHandler;

    public CheckOthersCommand(TimeHandler timeHandler, ConfigHandler configHandler, DatabaseHandler databaseHandler) {
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
        this.timeHandler = timeHandler;
    }

    public void checkOther(Player player, OfflinePlayer offlinePlayer) {
        if (player instanceof ConsoleCommandSender) {
            return;
        }

        if (!player.hasPermission("xcraftontime.check.others")) {
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.NoPermissionError());
            return;
        }

        UUID uuid = offlinePlayer.getUniqueId();
        Date joined = new Date(offlinePlayer.getFirstPlayed());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        if (offlinePlayer.isOnline()) {
            int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() -
                    timeHandler.cache.get(player.getUniqueId()).toEpochMilli(), TimeUnit.MILLISECONDS) +
                    databaseHandler.getPlaytime(uuid));
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                    " " + ChatColor.GOLD + configHandler.nameString() + ChatColor.RESET + ChatColor.BOLD +
                    ChatColor.RESET +
                    ChatColor.BOLD +
                    " " + offlinePlayer.getName());
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                    " " + ChatColor.GOLD + configHandler.playtimeString() + ChatColor.RESET + ChatColor.BOLD +
                    " " + TimeConverter.secondsToDays(time) +
                    " " + configHandler.daysString() +
                    " " + TimeConverter.secondsToHours(time) +
                    " " + configHandler.hoursString() +
                    " " + TimeConverter.secondsToMinutes(time) +
                    " " + configHandler.minutesString());
            if (player.hasPermission("xcraftontime.seeothersdate")) {
                player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                        " " + ChatColor.GOLD + configHandler.joinedAtString() + ChatColor.RESET + ChatColor.BOLD +
                        " " + DATE_FORMAT.format(joined));
            }

        } else {
            int time = databaseHandler.getPlaytime(uuid);
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                    " " + ChatColor.GOLD + configHandler.nameString() + ChatColor.RESET + ChatColor.BOLD +
                    ChatColor.RESET +
                    ChatColor.BOLD +
                    " " + databaseHandler.getPlayerName(offlinePlayer.getUniqueId()));
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                    " " + ChatColor.GOLD + configHandler.playtimeString() + ChatColor.RESET + ChatColor.BOLD +
                    " " + TimeConverter.secondsToDays(time) +
                    " " + configHandler.daysString() +
                    " " + TimeConverter.secondsToHours(time) +
                    " " + configHandler.hoursString() +
                    " " + TimeConverter.secondsToMinutes(time) +
                    " " + configHandler.minutesString());
            if (player.hasPermission("xcraftontime.seeothersdate")) {
                player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                        " " + ChatColor.GOLD + configHandler.joinedAtString() + ChatColor.RESET + ChatColor.BOLD +
                        " " + DATE_FORMAT.format(joined));
            }
        }
    }
}