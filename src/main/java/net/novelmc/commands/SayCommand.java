package net.novelmc.commands;

import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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

            for (String part : args)
            {
                if (msg != "") msg += " ";
                msg += part;
            }

            Bukkit.getServer().broadcastMessage(ChatColor.RED + "[Server:" + sender.getName() + "] " + msg);

        }

        return true;
    }
}
