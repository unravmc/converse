package net.novelmc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffworldCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.staffworld"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        World staffworld = Bukkit.getWorld("staffworld");
        if (staffworld == null)
        {
            sender.sendMessage("The staffworld could not be found. Please contact an administrator for assistance.");
            return true;
        }
        else
        {
            Player player = (Player)sender;
            sender.sendMessage(ChatColor.GRAY + "Teleporting to staffworld...");
            player.teleport(staffworld.getSpawnLocation());
        }
        return true;
    }
}
