package net.novelmc.commands;

import com.earth2me.essentials.User;
import java.io.IOException;
import net.novelmc.banning.Ban;
import net.novelmc.util.Util;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
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

        if (args.length == 0)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        User user;
        final String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        String banID = RandomStringUtils.randomAlphabetic(5);

        if (player == null)
        {
            player = offlinePlayer.getPlayer();
        }

        try
        {
            Ban.addBan(player, sender, banID, reason, "username");
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(), reason, null, sender.getName());
        player.kickPlayer(Ban.constructBanMessage(reason, banID));

        Util.action(sender, "Banning " + player.getName() + " with reason: " + reason);
        return true;
    }
}