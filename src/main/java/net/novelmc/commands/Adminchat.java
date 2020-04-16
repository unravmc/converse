package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import net.novelmc.commands.loader.Messages;
import net.novelmc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//This is how every command should look from now on!
@CommandParameters(description = "Talk privately with other staff members\\; also toggles it on or off.", usage = "/<command> [message]", aliases = "o, ac")
public class Adminchat extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.adminchat")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        String message = StringUtils.join(args, " ");
        if (message.length() == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Adminchat can only be toggled in-game.");
                return true;
            }
            Player p = (Player) sender;
            Util.putAdminChat(p.getUniqueId());
            p.sendMessage(ChatColor.GRAY + "Toggled adminchat " + (Util.isInAdminChat(p.getUniqueId()) ? "on" : "off"));
            return true;
        }
        Util.adminchat(sender, message);
        return true;
    }
}
