package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Teleport to the netherworld", usage = "/<command>", aliases = "nw")
public class Netherworld extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        World world_nether = Bukkit.getWorld("world_nether");
        if (world_nether == null) {
            sender.sendMessage(ChatColor.RED +
                    "The netherworld could not be found. Please contact an administrator for assistance.");
            return true;
        } else {
            Player player = (Player) sender;
            sender.sendMessage(ChatColor.GRAY + "Teleporting to the netherworld...");
            player.teleport(world_nether.getSpawnLocation());
        }
        return true;
    }
}
