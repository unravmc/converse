package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Teleport to the enderworld", usage = "/<command>", aliases = "ew")
public class Enderworld extends CommandBase {
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
