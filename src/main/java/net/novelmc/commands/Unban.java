package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.playerdata.PlayerData;
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
        if (!plugin.playerDataManager.doesPlayerDataExist(offlinePlayer.getUniqueId())) {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        if (!plugin.banManager.isPlayerBanned(offlinePlayer.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "That player does not have an active ban!");
            return true;
        }

        Util.action(sender, "Unbanning " + offlinePlayer.getName());
        plugin.banManager.removeBanFromHistory(plugin.banManager.getLatestBan(offlinePlayer.getUniqueId()));
        return true;
    }
}
