package me.umbreon.xcraftontime.commands;

import me.umbreon.xcraftontime.handlers.ConfigHandler;
import me.umbreon.xcraftontime.handlers.DatabaseHandler;
import me.umbreon.xcraftontime.handlers.TimeHandler;
import me.umbreon.xcraftontime.utils.TimeConverter;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CheckCommand {

    private final ConfigHandler configHandler;
    private final DatabaseHandler databaseHandler;
    private final TimeHandler timeHandler;

    public CheckCommand(ConfigHandler configHandler, DatabaseHandler databaseHandler, TimeHandler timeHandler) {
        this.configHandler = configHandler;
        this.databaseHandler = databaseHandler;
        this.timeHandler = timeHandler;
    }

    public void checkPlayerCommand(Player player) {
        if (player instanceof ConsoleCommandSender) {
            return;
        }

        if (!player.hasPermission("xcraftontime.check")) {
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" + " " + ChatColor.RED + configHandler.NoPermissionError());
            return;
        }

        Date joined = new Date(player.getFirstPlayed());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        int time = ((int) TimeUnit.SECONDS.convert(Calendar.getInstance().getTime().getTime() -
                timeHandler.cache.get(player.getUniqueId()).toEpochMilli(), TimeUnit.MILLISECONDS) +
                databaseHandler.getPlaytime(player.getUniqueId())
        );

        player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +  " " + ChatColor.GOLD + configHandler.nameString() + ChatColor.RESET + ChatColor.BOLD + " " + player.getName());

        player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                " " + ChatColor.GOLD + configHandler.playtimeString() + ChatColor.RESET + ChatColor.BOLD +
                " " + TimeConverter.secondsToDays(time) + " " + configHandler.daysString() +
                " " + TimeConverter.secondsToHours(time) + " " + configHandler.hoursString() +
                " " + TimeConverter.secondsToMinutes(time) + " " + configHandler.minutesString());

        if (player.hasPermission("xcraftontime.seedate")) {
            player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + configHandler.pluginPrefixString() + ChatColor.WHITE + "]" +
                    " " + ChatColor.GOLD + configHandler.joinedAtString() + ChatColor.RESET + ChatColor.BOLD +
                    " " + DATE_FORMAT.format(joined));
        }
    }
}
