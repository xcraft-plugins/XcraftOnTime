package me.umbreon.xcraftontime.handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleterHandler implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {

                if (player.hasPermission("xcraftontime.help")) {
                    if (args[0].toLowerCase().startsWith("h") ||
                            args[0].toLowerCase().startsWith("he") ||
                            args[0].toLowerCase().startsWith("hel")
                    ) {
                        completions.add("help");
                    }
                }

                if (player.hasPermission("xcraftontime.add")) {
                    if (args[0].toLowerCase().startsWith("a") ||
                            args[0].toLowerCase().startsWith("ad")
                    ) {
                        completions.add("add");
                    }
                }

                if (player.hasPermission("xcraftontime.remove")) {
                    if (args[0].toLowerCase().startsWith("r") ||
                            args[0].toLowerCase().startsWith("re") |
                            args[0].toLowerCase().startsWith("rem") ||
                            args[0].toLowerCase().startsWith("remo") ||
                            args[0].toLowerCase().startsWith("remov")
                    ) {
                        completions.add("remove");
                    }
                }

                if (player.hasPermission("xcraftontime.top")) {
                    if (args[0].toLowerCase().startsWith("t") ||
                            args[0].toLowerCase().startsWith("to")
                    ) {
                        completions.add("top");
                    }
                }

                if (player.hasPermission("xcraftontime.check.others")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(p.getName());
                        }
                    }
                }

            } else if (args.length == 2 && args[0].equals("add") && player.hasPermission("xcraftontime.add")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(p.getName());
                    }
                }

            } else if (args.length == 2 && args[0].equals("remove") && player.hasPermission("xcraftontime.remove")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(p.getName());
                    }
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
