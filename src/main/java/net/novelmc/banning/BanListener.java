package net.novelmc.banning;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BanListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        if (Ban.isBanned(player))
        {
            if (player.hasPermission("converse.ban.bypass"))
            {
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Ban.constructBanMessage(Ban.getReason(player), Ban.getBanID(player)));
        }
    }
}
