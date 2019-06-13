package net.novelmc.permban;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PermbanListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        if (Permban.isBanned(player))
        {
            if (player.hasPermission("converse.ban.bypass"))
            {
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Permban.constructBanMessage(Permban.getReason(player), Permban.getBanID(player)));
        }
    }
}
