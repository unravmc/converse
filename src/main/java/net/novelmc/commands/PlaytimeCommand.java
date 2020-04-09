package net.novelmc.commands;

import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
                sender.sendMessage(ChatColor.AQUA + "Your playtime: " + ChatColor.WHITE +
                        formattedTime(plugin.ptl.playtime.get(((Player) sender).getUniqueId())));
            } else {
                sender.sendMessage(ChatColor.RED + "This command is only executable by a player!");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
            if(target != null) {
                plugin.ptl.cachePlayerPlaytime(target);
                sender.sendMessage(ChatColor.AQUA + target.getName()+ "'s playtime: " + ChatColor.WHITE +
                        formattedTime(plugin.ptl.playtime.get(target.getUniqueId())));
            } else {
                if(plugin.ptl.playtime.containsKey(offlineTarget.getUniqueId())) {
                    sender.sendMessage(ChatColor.AQUA + offlineTarget.getName()+ "'s playtime: " + ChatColor.WHITE +
                            formattedTime(plugin.ptl.playtime.get(offlineTarget.getUniqueId())));
                } else {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                }
            }
        }

        return true;
    }

    public String formattedTime(Long timeMillis) {
        long seconds = timeMillis / 1000;
        long days = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days *24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        return String.format("%d day(s), %d hour(s), %d minute(s)", days, hours, minutes);
    }
}
