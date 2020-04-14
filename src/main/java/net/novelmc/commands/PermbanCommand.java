package net.novelmc.commands;

import net.novelmc.util.Util;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Permanently ban a player", usage = "/<command> <player> [reason]")
public class PermbanCommand extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.permban")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        String banID = RandomStringUtils.randomAlphabetic(5);

        if (player == null) {
            plugin.permban.addPermban(offlinePlayer, sender, banID, reason, "username");
            if (reason.length() == 0) {
                Util.action(sender, "Permanently banning " + offlinePlayer.getName());
                return true;
            } else {
                Util.action(sender, "Permanently banning " + offlinePlayer.getName() + " with reason: " + reason);
            }
        } else {
            plugin.permban.addPermban(player, sender, banID, reason, "username");
            player.kickPlayer(plugin.permban.constructBanMessage(reason, banID));
            if (reason.length() == 0) {
                Util.action(sender, "Permanently banning " + player.getName());
                return true;
            } else {
                Util.action(sender, "Permanently banning " + player.getName() + " with reason: " + reason);
            }
        }
        return true;
    }
}