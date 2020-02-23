package me.umbreon.xcraftontime.handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterHandler implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
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
