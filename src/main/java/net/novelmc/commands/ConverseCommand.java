package net.novelmc.commands;

import java.io.File;
import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConverseCommand extends ConverseBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (args.length == 0)
        {
            Converse.BuildProperties build = Converse.build;
            sender.sendMessage(ChatColor.GOLD + "Converse is a plugin by which the NovelMC server manages operator " +
                    "permissions, in-game functions, and staff member tools.");
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
        switch (args[0].toLowerCase())
        {
            case "debug":
            {
                if (!sender.hasPermission("converse.debug"))
                {
                    sender.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                Player player = (Player)sender;
                if (plugin.lp.isStaff(player.getUniqueId()))
                {
                    sender.sendMessage("You are staff!");
                }
                else
                {
                    sender.sendMessage("You aren't staff.");
                }
                sender.sendMessage(plugin.getDataFolder() + File.separator + "players" + File.separator + player.getName() + ".yml");

                return true;
            }
            case "reload":
            {
                if (!sender.hasPermission("converse.reload"))
                {
                    sender.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                plugin.registerConfigs();
                sender.sendMessage(ChatColor.GRAY + "All Converse files have been reloaded.");
                return true;
            }
            default:
                return false;
        }
    }
}
