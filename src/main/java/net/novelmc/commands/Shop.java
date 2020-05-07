package net.novelmc.commands;

import net.novelmc.ConversePlugin;
import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.util.Punisher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Opens the shop menu", usage = "/<command>", aliases = "umcshop")
public class Shop extends CommandBase {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by players.");
            return true;
        }

        Player p = (Player) sender;
        ConversePlugin.plugin.shop.open(p);
        return true;
    }
}