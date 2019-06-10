package net.novelmc.util;

import java.util.ArrayList;
import java.util.List;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util
{
    public static List<String> getOnlinePlayers()
    {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            players.add(player.getName());
        }
        return players;
    }

    public static void action(Player player, String message)
    {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + player.getName() + ": " + message + "]");
    }

    public static void action(CommandSender sender, String message)
    {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender.getName() + ": " + message + "]");
    }

    public static void action(String sender, String message)
    {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender + ": " + message + "]");
    }

    public static String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    public static void adminchat(CommandSender sender, String message)
    {
        Bukkit.getLogger().info("[AdminChat] " + sender.getName() + ": " + message);
        String badAdminChatFormat = ChatColor.DARK_GRAY + "# " + LuckPermsBridge.displayRank(Bukkit.getPlayer(sender.getName()).getUniqueId())
                + ChatColor.GRAY + " " + sender.getName() + ChatColor.DARK_GRAY
                + ": " + ChatColor.GOLD + message;
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.hasPermission("converse.adminchat"))
            {
                player.sendMessage(badAdminChatFormat);
            }
        }
    }
}
