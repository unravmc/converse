package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StaffListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        changeTabColor(event.getPlayer());
        displayLoginMessage(event.getPlayer());
    }

    private void changeTabColor(Player player)
    {
        UUID uuid = player.getUniqueId();
        if (LuckPermsBridge.isModerator(uuid))
        {
            player.setPlayerListName(ChatColor.BLUE + player.getName());
        }
        else if (LuckPermsBridge.isSeniorModerator(uuid))
        {
            player.setPlayerListName(ChatColor.GOLD + player.getName());
        }
        else if (LuckPermsBridge.isDeveloper(uuid))
        {
            player.setPlayerListName(ChatColor.DARK_PURPLE + player.getName());
        }
        else if (LuckPermsBridge.isExecutive(uuid))
        {
            player.setPlayerListName(ChatColor.RED + player.getName());
        }
        else if (LuckPermsBridge.isArchitect(uuid))
        {
            player.setPlayerListName(ChatColor.DARK_GREEN + player.getName());
        }
    }

    private void displayLoginMessage(Player player)
    {
        UUID uuid = player.getUniqueId();
        if (LuckPermsBridge.isModerator(uuid))
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() +
                    " is a " + ChatColor.BLUE + "" + ChatColor.ITALIC + "Moderator");
        }
        else if (LuckPermsBridge.isSeniorModerator(uuid))
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() +
                    " is a " + ChatColor.GOLD + "" + ChatColor.ITALIC + "Senior Moderator");
        }
        else if (LuckPermsBridge.isDeveloper(uuid))
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() +
                    " is a " + ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Developer");
        }
        else if (LuckPermsBridge.isExecutive(uuid))
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + "" +
                    player.getName() + " is an " + ChatColor.RED + "" + ChatColor.ITALIC + "Executive");
        }
        else if (LuckPermsBridge.isArchitect(uuid))
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + "" +
                    player.getName() + " is an " + ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "Architect");
        }
    }
}
