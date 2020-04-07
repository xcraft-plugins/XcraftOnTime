#XcraftOnTime
Saves the playing time of each player on a network.

Aliases: [ontime, ontimetracker]
##Installing
This plugin requires a MySQL database and the plugin [Essentials](https://github.com/EssentialsX/Essentials).

##Commands:
- /ontime - Shows you your playing time.
- /ontime [Name of Player] - Shows the playing time of the specified player.
- /ontime top - Shows the players with the highest playing time.
- /ontime help - Shows you the help page.
- /ontime delete [Name of Player] - Deletes all player data from the database.
- /ontime move [Name of Player] [Name of Target] - Moves the playing time from one player to another.
- /ontime add [Name of Player] [Time in seconds] - Adds the specified playing time to the player.
- /ontime remove [Name of Player] [Time in seconds] - Removes the specified playing time from the player.

##Permissions:
- xcraftontime.check - Command: /ontime
- xcraftontime.check.others - Command: /ontime [Name of Player]
- xcraftontime.top - Command: /ontime top
- xcraftontime.delete - Command: /ontime delete [Name of Player]
- xcraftontime.move - Command: /ontime move [Name of Player] [Name of Target]
- xcraftontime.add - Command: /ontime add [Name of Player] [Time in seconds]
- xcraftontime.remove - Command: /ontime remove [Name of Player] [Time in seconds]