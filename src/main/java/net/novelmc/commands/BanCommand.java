package net.novelmc.commands;

import java.util.Date;
import net.novelmc.bans.Ban;
import net.novelmc.util.Util;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.ban"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length < 2)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        Date expires = Util.parseDateOffset(args[1]);
        String banID = RandomStringUtils.randomAlphabetic(5);
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ");


        if (player == null)
        {
            Ban.addBan(offlinePlayer, sender, banID, reason, expires, "username");
            if (reason.length() == 0)
            {
                Util.action(sender, "Banning " + offlinePlayer.getName() + " until " + expires);
                return true;
            }
            else
            {
                Util.action(sender, "Banning " + offlinePlayer.getName() + " until " + expires + " with reason: " + reason);
            }
        }
        else
        {
            Ban.addBan(player, sender, banID, reason, expires, "username");
            player.kickPlayer(Ban.constructBanMessage(player, reason, banID));
            if (reason.length() == 0)
            {
                Util.action(sender, "Banning " + player.getName() + " until " + expires);
                return true;
            }
            else
            {
                Util.action(sender, "Banning " + player.getName() + " until " + expires + " with reason: " + reason);
            }
        }
        return true;
    }
}