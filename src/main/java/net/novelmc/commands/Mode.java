package net.novelmc.commands;

import net.novelmc.ConversePlugin;
import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Change the server mode", usage = "/<command> [event | dev | staff | off]")
public class Mode extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 1) {
            return false;
        }

        String mode = ConversePlugin.plugin.config.getString("mode");

        if (args.length == 1) {
            switch (args[0]) {
                case "dev": {
                    if (!sender.hasPermission("converse.mode.dev")) {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (mode.equalsIgnoreCase("dev")) {
                        plugin.ml.disableDevMode();
                    } else {
                        plugin.ml.enableDevMode();
                    }
                    return true;
                }
                case "event": {
                    if (!sender.hasPermission("converse.mode.event")) {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (mode.equalsIgnoreCase("event")) {
                        plugin.ml.disableEventMode();
                        return true;
                    } else {
                        plugin.ml.enableEventMode();
                        return true;
                    }
                }
                case "staff": {
                    if (!sender.hasPermission("converse.mode.staff")) {
                        sender.sendMessage(Messages.NO_PERMISSION);
                        return true;
                    }
                    if (mode.equalsIgnoreCase("staff")) {
                        plugin.ml.disableStaffMode();
                    } else {
                        plugin.ml.enableStaffMode();
                    }
                    return true;
                }
                case "off": {
                    if (mode.equalsIgnoreCase("default")) {
                        sender.sendMessage(ChatColor.RED + "You are already in the default mode!");
                        return true;
                    }
                    ConversePlugin.plugin.config.set("mode", "default");
                    Util.action(sender, "The server has re-opened to everyone.");
                    return true;
                }
                default:
                    return false;
            }
        }
        sender.sendMessage(ChatColor.GRAY + "Available server modes:");
        sender.sendMessage(ChatColor.GOLD + " - Dev: Plugin testing status for leadership and developers.");
        sender.sendMessage(ChatColor.GOLD + " - Staff: Restricts the server to staff members only.");
        sender.sendMessage(ChatColor.GOLD + " - Event: Whitelist all online players and closes the server.");
        sender.sendMessage(ChatColor.GOLD + " - Default: Normal server functionality.");
        sender.sendMessage(ChatColor.GRAY + "The server is currently running in " + mode + " mode.");
        return true;
    }
}
