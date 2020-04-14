package net.novelmc.commands;

import net.novelmc.playerdata.PlayerData;
import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@CommandParameters(description = "View player playtime", usage = "/<command> [player]")
public class PlaytimeCommand extends CommandBase {
    @SuppressWarnings("deprecated")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                plugin.ptl.cachePlayerPlaytime((Player) sender);
                PlayerData pData = plugin.playerDataManager.getPlayerData(((Player) sender));
                sender.sendMessage(ChatColor.GRAY + "Your playtime: " + ChatColor.GOLD +
                        formattedTime(pData.getPlaytime()));
            } else {
                sender.sendMessage(ChatColor.RED + "This command is only executable by a player!");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                plugin.ptl.cachePlayerPlaytime(target);
                PlayerData pData = plugin.playerDataManager.getPlayerData((target));
                sender.sendMessage(ChatColor.GRAY + target.getName()+ "'s playtime: " + ChatColor.GOLD +
                        formattedTime(pData.getPlaytime()));
            } else {
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
                if (plugin.playerDataManager.doesPlayerDataExist(offlineTarget.getUniqueId())) {
                    PlayerData pData = plugin.playerDataManager.getPlayerData(offlineTarget);
                    sender.sendMessage(ChatColor.GRAY + offlineTarget.getName() + "'s playtime: " + ChatColor.GOLD +
                            formattedTime(pData.getPlaytime()));
                } else {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                }
            }
        }

        return true;
    }

    public String formattedTime(Long timeMillis) {
        long seconds = timeMillis / 1000;
        long days = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        return String.format("%d day(s), %d hour(s), %d minute(s)", days, hours, minutes);
    }
}
