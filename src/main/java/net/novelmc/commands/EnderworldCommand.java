package net.novelmc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderworldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        World world_the_end = Bukkit.getWorld("world_the_end");
        if (world_the_end == null) {
            sender.sendMessage(ChatColor.RED +
                    "The enderworld could not be found. Please contact an administrator for assistance.");
            return true;
        } else {
            Player player = (Player) sender;
            sender.sendMessage(ChatColor.GRAY + "Teleporting to the enderworld...");
            player.teleport(world_the_end.getSpawnLocation());
        }
        return true;
    }
}
