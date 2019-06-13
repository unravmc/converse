package net.novelmc.commands;

import net.novelmc.permban.Permban;
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

public class PermbanCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.ban"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        String banID = RandomStringUtils.randomAlphabetic(5);

        if (player == null)
        {
            Permban.addBan(offlinePlayer, sender, banID, reason, "username");
            if (reason.length() == 0)
            {
                Util.action(sender, "Banning " + offlinePlayer.getName());
                return true;
            }
            else
            {
                Util.action(sender, "Banning " + offlinePlayer.getName() + " with reason: " + reason);
            }
        }
        else
        {
            Permban.addBan(player, sender, banID, reason, "username");
            player.kickPlayer(Permban.constructBanMessage(reason, banID));
            if (reason.length() == 0)
            {
                Util.action(sender, "Banning " + player.getName());
                return true;
            }
            else
            {
                Util.action(sender, "Banning " + player.getName() + " with reason: " + reason);
            }
        }
        return true;
    }
}