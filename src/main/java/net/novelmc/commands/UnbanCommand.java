package net.novelmc.commands;

import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnbanCommand extends ConverseBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.unban")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (plugin.ban.removeBan(offlinePlayer)) {
            Util.action(sender, "Unbanning " + offlinePlayer.getName());
            return true;
        } else if (plugin.permban.removePermban(offlinePlayer)) {
            Util.action(sender, "Unbanning " + offlinePlayer.getName());
            return true;
        } else {
            sender.sendMessage(ChatColor.GRAY + "Converse could not find a ban under that name.");
        }
        return true;
    }
}
