package net.novelmc.listeners;

import net.novelmc.bans.Ban;
import net.novelmc.permban.Permban;
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

        if (Permban.isBanned(player))
        {
            if (player.hasPermission("converse.ban.bypass"))
            {
                Permban.removePermban(player);
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Permban.constructBanMessage(Permban.getReason(player), Permban.getBanID(player)));
        }

        if (Ban.isBanned(player))
        {
            Ban.removeBan(player);
        }
        else
        {
            if (Ban.get(player))
            {
                if (player.hasPermission("converse.ban.bypass"))
                {
                    Ban.removeBan(player);
                    return;
                }
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Ban.constructBanMessage(player, Ban.getReason(player), Ban.getBanID(player)));
            }
        }
    }
}
