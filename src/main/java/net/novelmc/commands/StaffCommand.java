package net.novelmc.commands;

import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import net.novelmc.permissions.Permissions;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor
{
    static LuckPermsApi api = Converse.getLuckPermsAPI();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("add"))
        {
            if (args.length < 3)
            {
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);

            if (player == null)
            {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                return true;
            }

            if (args[2].equalsIgnoreCase("moderator") || args[2].equalsIgnoreCase("mod"))
            {
                LuckPermsBridge.set(player.getUniqueId(), "mod");
                Util.action((Player)sender, "Adding " + player.getName() + " to Moderator");
                return true;
            }
        }
        else if (args[0].equalsIgnoreCase("remove"))
        {
            if (args.length < 2)
            {
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);

            if (player == null)
            {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                return true;
            }

            LuckPermsBridge.set(player.getUniqueId(), "op");
            Util.action((Player)sender, "Removing " + player.getName() + " from staff");
            return true;
        }
        else
        {
            sender.sendMessage("gay");
        }
        return true;
    }
}
