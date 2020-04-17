package net.novelmc.commands;

import net.novelmc.bans.BanData;
import net.novelmc.bans.BanType;
import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@CommandParameters(description = "Lookup a ban by id", usage = "/<command> <id>", aliases = "bl")
public class BanLookup extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("converse.banlookup")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        String id = args[0];
        if (plugin.banManager.getBanFromID(id) == null) {
            sender.sendMessage(ChatColor.RED + "That ID does not have an associated ban!");
            return true;
        }

        BanData ban = plugin.banManager.getBanFromID(id);

        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + "#" + ban.getBanID() + ChatColor.DARK_GRAY + " ]");
        sb.append("\n");
        sb.append(ChatColor.GRAY + "Staff Member: " + ChatColor.GOLD + (ban.getStaffUUID() != null ? Bukkit.getOfflinePlayer(ban.getStaffUUID()) : "CONSOLE"));
        sb.append("\n");
        sb.append(ChatColor.GRAY + "Expiration: " + ChatColor.GOLD + (ban.getBanType() != BanType.PERMANENT ? plugin.banManager.formatDate(ban.getBanExpiration()) : "Never"));
        sb.append("\n");
        sb.append(ChatColor.GRAY + "Issued At: " + ChatColor.GOLD + plugin.banManager.formatDate(ban.getDateIssued()));
        sb.append("\n");
        sb.append(ChatColor.GRAY + "Reason: " + ChatColor.GOLD + (ban.getReason() != null ? ban.getReason() : "No reason"));
        sb.append("\n");

        sender.sendMessage(sb.toString());

        return true;
    }
}
