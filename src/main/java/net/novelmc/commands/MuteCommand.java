package net.novelmc.commands;

import net.novelmc.bridge.LuckPermsBridge;
import net.novelmc.listeners.MuteListener;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.mute"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("purge"))
        {
            int amount = MuteListener.getMutedAmount();
            MuteListener.purgeMuted();
            Util.action(sender, "Unmuting all players");
            sender.sendMessage(ChatColor.GRAY + "Unmuted " + amount + " players.");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        if (LuckPermsBridge.isStaff(player.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + "You cannot mute admins.");
            return true;
        }
        MuteListener.setMuted(player, !MuteListener.isMuted(player));
        Util.action(sender, (MuteListener.isMuted(player) ? "M" : "Unm") + "uting " + player.getName());
        return true;
    }
}
