package net.novelmc.util;

import java.util.ArrayList;
import java.util.List;
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
}
