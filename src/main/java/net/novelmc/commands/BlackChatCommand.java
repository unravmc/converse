package net.novelmc.commands;

import net.novelmc.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlackChatCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.graychat"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        if (!ChatListener.isPunished(player))
        {
            ChatListener.setPunished(player, true);
            sender.sendMessage(ChatColor.GRAY + "Turning on gray chat for " + player.getName() + ".");
            return true;
        }
        else
        {
            ChatListener.setPunished(player, false);
            sender.sendMessage(ChatColor.GRAY + "Turning off gray chat for " + player.getName() + ".");
            return true;
        }
    }
}
