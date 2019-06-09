package net.novelmc.commands;

import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor
{
    private static LuckPermsApi api = Converse.getLuckPermsAPI();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (!sender.hasPermission("converse.staff"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0)
        {
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "add":
            {
                if (args.length < 3)
                {
                    return false;
                }

                Player player = Bukkit.getPlayer(args[1]);

                if (player == null)
                {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                // TODO: make it so that you cant move yourself to a higher rank
                if (args[2].equalsIgnoreCase("moderator") || args[2].equalsIgnoreCase("mod"))
                {
                    LuckPermsBridge.set(player.getUniqueId(), Converse.plugin.getConfig().getString("permissions.moderator"));
                    Util.action(sender, "Adding " + player.getName() + " to Moderator");
                    return true;
                }
                else if (args[2].equalsIgnoreCase("seniormod") || args[2].equalsIgnoreCase("srmod"))
                {
                    if (LuckPermsBridge.isModerator(player.getUniqueId()))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    LuckPermsBridge.set(player.getUniqueId(), Converse.plugin.getConfig().getString("permissions.senior_moderator"));
                    Util.action(sender, "Adding " + player.getName() + " to Senior Moderator");
                    return true;
                }
                else if (args[2].equalsIgnoreCase("developer") || args[2].equalsIgnoreCase("dev"))
                {
                    if (LuckPermsBridge.isModerator(player.getUniqueId()))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (LuckPermsBridge.isSeniorModerator(player.getUniqueId()))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    LuckPermsBridge.set(player.getUniqueId(), Converse.plugin.getConfig().getString("permissions.developer"));
                    Util.action(sender, "Adding " + player.getName() + " to Developer");
                    return true;
                }
                else if (args[2].equalsIgnoreCase("executive") || args[2].equalsIgnoreCase("exec"))
                {
                    if (LuckPermsBridge.isModerator(player.getUniqueId()))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (LuckPermsBridge.isSeniorModerator(player.getUniqueId()))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (LuckPermsBridge.isDeveloper(player.getUniqueId()))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    LuckPermsBridge.set(player.getUniqueId(), Converse.plugin.getConfig().getString("permissions.executive"));
                    Util.action(sender, "Adding " + player.getName() + " to Executive");
                    return true;
                } else
                {
                    sender.sendMessage(ChatColor.RED + "Please enter a valid rank!");
                    return true;
                }
            }
            case "remove":
            {
                if (args.length < 2)
                {
                    return false;
                }

                Player player = Bukkit.getPlayer(args[1]);

                if (player == null)
                {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                // TODO: make sure you cant remove someone higher than you
                LuckPermsBridge.set(player.getUniqueId(), "op");
                Util.action(sender, "Removing " + player.getName() + " from staff");
                return true;
            }
            default:
                return false;
        }
    }
}
