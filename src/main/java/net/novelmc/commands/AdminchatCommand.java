package net.novelmc.commands;

import net.novelmc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminchatCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.adminchat"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        String message = StringUtils.join(args, " ");
        if (message.length() == 0)
        {
            sender.sendMessage(ChatColor.GRAY + "Please enter a message.");
            return true;
        }
        Util.adminchat(sender, message);
        return true;
    }
}
