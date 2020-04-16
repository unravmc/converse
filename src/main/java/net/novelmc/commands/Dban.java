package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.util.Util;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandParameters(description = "Bans a player for a day", usage = "/<command> <player> [reason]", aliases = "dayban, gtfo")
public class Dban extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.dban")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length < 1) {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        Date expires = Util.parseDateOffset("24h");
        String banID = RandomStringUtils.randomAlphabetic(5);

        if (player == null) {
            plugin.ban.addBan(offlinePlayer, sender, banID, reason, expires, "username");
            if (reason.length() == 0) {
                Util.action(sender, "Banning " + offlinePlayer.getName());
                return true;
            } else {
                Util.action(sender, "Banning " + offlinePlayer.getName() + " with reason: " + reason);
            }
        } else {
            plugin.ban.addBan(player, sender, banID, reason, expires, "username");
            player.kickPlayer(plugin.ban.constructBanMessage(player, reason, banID));
            if (reason.length() == 0) {
                Util.action(sender, "Banning " + player.getName());
                return true;
            } else {
                Util.action(sender, "Banning " + player.getName() + " with reason: " + reason);
            }
        }
        return true;
    }
}