package net.novelmc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldListener implements Listener
{
    // UNTESTED
    private World staffWorld = Bukkit.getWorld("staffworld");
    private World world = Bukkit.getWorld("world");

    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.getTo().getWorld().getName().equalsIgnoreCase("staffworld"))
        {
            if (staffWorld == null)
            {
                return;
            }
            else
            {
                if (!event.getPlayer().hasPermission("converse.staffworld"))
                {
                    event.getPlayer().sendMessage(ChatColor.GRAY + "That player is in the staff world. " +
                            "You are not allowed to access the staff world.");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (event.getPlayer().getLocation().getWorld().equals(staffWorld))
        {
            if (!event.getPlayer().hasPermission("converse.staffworld"))
            {
                if (world == null)
                {
                    Bukkit.getLogger().severe("The server is fucked and something needs to be done ASAP.");
                    return;
                }
                event.getPlayer().teleport(world.getSpawnLocation());
                event.getPlayer().sendMessage(ChatColor.GRAY + "You were in the staff world. You no longer have " +
                        "permission to access this world, so you were teleported to spawn.");
            }
        }
    }
}
