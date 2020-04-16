package net.novelmc.commands;

import java.util.UUID;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Look up a player ban", usage = "/<command> <player>", aliases = "bl")
public class BanLookup extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.banlookup")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        UUID targetUUID = target.getUniqueId();

        if (plugin.ban.getPlayer(targetUUID)) {
            StringBuilder output = new StringBuilder();
            output.append(ChatColor.GOLD + "------- " + ChatColor.RED + target.getName() + ChatColor.GOLD + " -------" +
                    "\n");
            if (plugin.ban.hasIP(target)) {
                output.append(ChatColor.GOLD + " > IP Address: " + plugin.ban.getIP(target) + "\n");
            }
            output.append(ChatColor.GOLD + " > Banned by: " + plugin.ban.getBannedBy(target) + "\n");
            if (plugin.ban.hasReason(target)) {
                output.append(ChatColor.GOLD + " > Reason: " + plugin.ban.getReason(target) + "\n");
            }
            if (plugin.ban.hasExpiry(target)) {
                output.append(ChatColor.GOLD + " > Duration: " + plugin.ban.formatDate(target) + "\n");
            }
            output.append(ChatColor.GOLD + " > Ban ID: " + plugin.ban.getBanID(target) + "\n");
            sender.sendMessage(output.toString());
            return true;
        } else if (plugin.permban.isBanned(target)) {
            StringBuilder output = new StringBuilder();
            output.append(ChatColor.GOLD + "------- " + ChatColor.RED + target.getName() + ChatColor.GOLD + " -------" +
                    "\n");

        }


        return true;
    }
}
