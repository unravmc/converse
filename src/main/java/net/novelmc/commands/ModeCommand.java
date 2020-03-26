package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ModeCommand extends ConverseBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 1) {
            return false;
        }

        String mode = Converse.plugin.config.getString("mode");

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
                    Converse.plugin.config.set("mode", "default");
                    Util.action(sender, "The server has re-opened to everyone.");
                    return true;
                }
                default:
                    return false;
            }
        }
        sender.sendMessage(ChatColor.BLUE + "Available server modes:");
        sender.sendMessage(ChatColor.GOLD + " - Dev mode: Plugin testing for leadership and developers.");
        sender.sendMessage(ChatColor.GOLD + " - Staff mode: Staff only mode.");
        sender.sendMessage(ChatColor.GOLD + " - Event mode: Whitelist all online players and close the server.");
        sender.sendMessage(ChatColor.GOLD + " - Default mode: Normal server functionality.");
        sender.sendMessage(ChatColor.BLUE + "The server is currently running in " + mode + " mode.");
        return true;
    }
}
