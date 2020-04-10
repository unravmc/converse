package net.novelmc.commands;

import net.novelmc.playerdata.PlayerData;
import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlaytimeCommand extends ConverseBase implements CommandExecutor {
    @SuppressWarnings("deprecated")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                plugin.ptl.cachePlayerPlaytime((Player) sender);
                PlayerData pData = plugin.playerDataManager.getPlayerData(((Player) sender));
                sender.sendMessage(ChatColor.AQUA + "Your playtime: " + ChatColor.WHITE +
                        formattedTime(pData.getPlaytime()));
            } else {
                sender.sendMessage(ChatColor.RED + "This command is only executable by a player!");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                plugin.ptl.cachePlayerPlaytime(target);
                PlayerData pData = plugin.playerDataManager.getPlayerData((target));
                sender.sendMessage(ChatColor.AQUA + target.getName()+ "'s playtime: " + ChatColor.WHITE +
                        formattedTime(pData.getPlaytime()));
            } else {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            }
        }

        return true;
    }

    public String formattedTime(Long timeMillis) {
        long seconds = timeMillis / 1000;
        long days = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
        return String.format("%d day(s), %d hour(s), %d minute(s)", days, hours, minutes);
    }
}
