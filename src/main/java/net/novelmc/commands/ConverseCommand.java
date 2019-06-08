package net.novelmc.commands;

import java.util.Collections;
import java.util.List;
import net.novelmc.Converse;
import net.novelmc.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ConverseCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.converse"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0)
        {
            Converse.BuildProperties build = Converse.build;
            sender.sendMessage(ChatColor.GOLD + "Converse");
            sender.sendMessage(ChatColor.GOLD + String.format("Version "
                            + ChatColor.BLUE + "%s.%s.%s",
                    build.version,
                    build.number,
                    build.head));
            sender.sendMessage(String.format(ChatColor.GOLD + "Compiled on "
                            + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                            + ChatColor.BLUE + "%s",
                    build.date,
                    build.author));
            sender.sendMessage(ChatColor.GREEN + "Visit " + ChatColor.AQUA + "https://github.com/novelmc/converse" + ChatColor.GREEN + " for more information");
            return true;
        }
        if (args[0].equalsIgnoreCase("debug"))
        {
            Permissions.informIfMod(sender, ((Player)sender).getUniqueId());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return Collections.singletonList("debug");
    }
}
