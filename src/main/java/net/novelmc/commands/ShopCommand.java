package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.util.AprilFools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by players.");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                AprilFools.start();
                Bukkit.broadcastMessage("April Fools mode has been enabled!");
                return true;
            }
            else if (args[0].equalsIgnoreCase("stop")) {
                AprilFools.stop();
                Bukkit.broadcastMessage("April Fools mode has been disabled!");
                return true;
            }
        }

        Player p = (Player) sender;
        Converse.plugin.shop.open(p);
        return true;
    }
}