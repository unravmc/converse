package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Unbans a banned player",
        usage = "/<command> <player>",
        aliases = "pardon, epardon, unpermban")
public class Unban extends CommandBase {
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
            sender.sendMessage(ChatColor.RED + "ConversePlugin could not find a ban under that name!");
        }
        return true;
    }
}
