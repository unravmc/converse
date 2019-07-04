package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StaffListener implements Listener
{
    private Converse plugin;

    public StaffListener(Converse plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        changeTabColor(event.getPlayer());
    }

    private void changeTabColor(Player player)
    {
        UUID uuid = player.getUniqueId();
        if (plugin.lp.isStaff(uuid))
        {
            player.setPlayerListName(plugin.lp.displayRankColor(player) + player.getName());
        }
    }
}
