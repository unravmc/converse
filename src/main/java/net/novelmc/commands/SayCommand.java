package net.novelmc.commands;

import net.novelmc.util.ConverseBase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SayCommand extends ConverseBase implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args)
    {
        if (!sender.hasPermission("converse.say"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        String msg = "";

        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "Incorrect usage: /say <text>");
            return false;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GOLD + "[" + "Server:" + sender.getName() + "] ");
            Arrays.stream(args).forEach(argument -> {
               builder.append(argument);
            });
            Bukkit.broadcastMessage(builder.toString());

        }

        return true;
    }
}
