package net.novelmc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class WorldListener implements Listener
{
    // UNTESTED
    private World staffWorld = Bukkit.getWorld("staffworld");
    private World world = Bukkit.getWorld("world");

    @EventHandler
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event)
    {
        if (event.getPlayer().getWorld() == staffWorld)
        {
            if (!event.getPlayer().hasPermission("converse.staffworld"))
            {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Sorry, but you cannot access the staff world.");
                event.getPlayer().teleport(world.getSpawnLocation());
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event)
    {
        if (staffWorld == null)
        {
            return;
        }
        if (event.getPlayer().getLocation().getWorld().getName().equals(staffWorld.toString()))
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
