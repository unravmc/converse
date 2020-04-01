package net.novelmc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoterworldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        World voterworld = Bukkit.getWorld("voterworld");
        if (voterworld == null) {
            sender.sendMessage(ChatColor.RED +
                    "The voterworld could not be found. Please contact an administrator for assistance.");
            return true;
        } else {
            Player player = (Player) sender;
            sender.sendMessage(ChatColor.GRAY + "Teleporting to the voterworld...");
            player.teleport(voterworld.getSpawnLocation());
        }
        return true;
    }
}
