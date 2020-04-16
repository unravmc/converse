package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Teleport to the staff world.",
        usage = "/<command> <add <player> | remove <player>>",
        aliases = "adminworld, aw, sw")
public class Staffworld extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.staffworld")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        World staffworld = Bukkit.getWorld("staffworld");
        if (staffworld == null) {
            sender.sendMessage(ChatColor.RED +
                    "The staffworld could not be found. Please contact an administrator for assistance.");
            return true;
        } else {
            Player player = (Player) sender;

            if (args.length == 0) {
                sender.sendMessage(ChatColor.GRAY + "Teleporting to the staffworld...");
                player.teleport(staffworld.getSpawnLocation());
                return true;
            } else {
                switch (args[0].toLowerCase()) {
                    case "add": {
                        if (args.length < 2) {
                            return false;
                        }
                        Player sPlayer = Bukkit.getPlayer(args[1]);
                        if (sPlayer == null) {
                            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                            return true;
                        }
                        if (plugin.lp.isStaff(sPlayer.getUniqueId()) ||
                                plugin.lp.isArchitect(sPlayer.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "That player already has access to the staff world.");
                            return true;
                        }
                        plugin.lp.allowStaffWorld(sPlayer.getUniqueId());
                        Util.action(sender, "Adding " + sPlayer.getName() + " to the staff world");
                        return true;
                    }
                    case "remove": {
                        if (args.length < 2) {
                            return false;
                        }
                        Player sPlayer = Bukkit.getPlayer(args[1]);
                        if (sPlayer == null) {
                            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                            return true;
                        }
                        if (plugin.lp.isStaff(sPlayer.getUniqueId()) ||
                                plugin.lp.isArchitect(sPlayer.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "You cannot remove access from that player.");
                            return true;
                        }
                        plugin.lp.disallowStaffWorld(sPlayer.getUniqueId());
                        Util.action(sender, "Removing " + sPlayer.getName() + " from the staff world");
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
