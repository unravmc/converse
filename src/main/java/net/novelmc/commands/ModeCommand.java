package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.listeners.ModeListener;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ModeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (args.length > 1)
        {
            return false;
        }

        String mode = Converse.plugin.getConfig().getString("mode");

        if (args.length == 1)
        {
            switch (args[0])
            {
                case "dev":
                {
                    if (!sender.hasPermission("converse.mode.dev"))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (mode.equalsIgnoreCase("dev"))
                    {
                        ModeListener.disableDevMode();
                    }
                    else
                    {
                        ModeListener.enableDevMode();
                    }
                    return true;
                }
                case "event":
                {
                    if (!sender.hasPermission("converse.mode.event"))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (mode.equalsIgnoreCase("event"))
                    {
                        ModeListener.disableEventMode();
                        return true;
                    }
                    else
                    {
                        ModeListener.enableEventMode();
                        return true;
                    }
                }
                case "staff":
                {
                    if (!sender.hasPermission("converse.mode.staff"))
                    {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (mode.equalsIgnoreCase("staff"))
                    {
                        ModeListener.disableStaffMode();
                    }
                    else
                    {
                        ModeListener.enableStaffMode();
                    }
                    return true;
                }
                case "off":
                {
                    if (mode.equalsIgnoreCase("default"))
                    {
                        sender.sendMessage(ChatColor.RED + "You are already in the default mode!");
                        return true;
                    }
                    Converse.plugin.getConfig().set("mode", "default");
                    Util.action(sender, "The server has re-opened to everyone.");
                    return true;
                }
                default:
                    return false;
            }
        }
        sender.sendMessage(Util.colorize("&7Server Modes&8:"));
        sender.sendMessage(Util.colorize("&8<-> &5&ldev&r&8: &7Plugin testing for leadership and devs."));
        sender.sendMessage(Util.colorize("&8<-> &4&lstaff&r&8: &7Staff-only mode."));
        sender.sendMessage(Util.colorize("&8<-> &9&levent&r&8: &7Event mode."));
        sender.sendMessage(Util.colorize("&8<-> &7&loff&r&8: &7Normal mode functionality."));
        sender.sendMessage(Util.colorize("&8<-> &7The server is currently running in " + mode + " mode."));
        return true;
    }
}
