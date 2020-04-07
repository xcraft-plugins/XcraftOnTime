#XcraftOnTime

Minecraft player online time tracker for Spigot servers.


##Installing

This plugin requires a MySQL database and the plugin [Essentials](https://github.com/EssentialsX/Essentials).


##Commands

Aliases: \[ontime, ontimetracker]

- `/ontime` - Shows you your playing time.
- `/ontime <Name of Player>` - Shows the playing time of the specified player.
- `/ontime top` - Shows the players with the highest playing time.
- `/ontime add <Name of Player> <Time in seconds>` - Adds the specified playing time to the player.
- `/ontime remove <Name of Player> <Time in seconds>` - Removes the specified playing time from the player.
- `/ontime clear <Name of Player>` - Deletes player's data from the database.
- `/ontime reload` - Reloads the config file.
- `/ontime help` or `/ontime info` - Shows you the help page.


##Permissions

- `xcraftontime.check` - Command: `/ontime`
- `xcraftontime.check.others` - Command: `/ontime <Name of Player>`
- `xcraftontime.top` - Command: `/ontime top`
- `xcraftontime.add` - Command: `/ontime add <Name of Player> <Time in seconds>`
- `xcraftontime.remove` - Command: `/ontime remove <Name of Player> <Time in seconds>`
- `xcraftontime.clear` - Command: `/ontime clear <Name of Player>`
