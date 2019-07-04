package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.util.ConverseBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StaffListener extends ConverseBase implements Listener
{
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
