package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.util.AprilFools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Opens the shop menu", usage = "/<command>", aliases = "umcshop")
public class ShopCommand extends CommandBase {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by players.");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                AprilFools.start();
                return true;
            } else if (args[0].equalsIgnoreCase("stop")) {
                AprilFools.stop();
                return true;
            } else {
                return false;
            }
        }

        Player p = (Player) sender;
        Converse.plugin.shop.open(p);
        return true;
    }
}