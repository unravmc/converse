package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.bridge.LuckPermsBridge;
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
    }

    // clean up, unify staff colors into one thing
    public static void changeTabColor(Player player)
    {
        UUID uuid = player.getUniqueId();
        if (LuckPermsBridge.isModerator(uuid))
        {
            player.setPlayerListName(ChatColor.DARK_GREEN + player.getName());
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
            player.setPlayerListName(ChatColor.BLUE + player.getName());
        }
    }
}
