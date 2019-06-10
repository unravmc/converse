package net.novelmc.banning;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
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
        player.sendMessage("this works");
        if (player.isBanned())
        {
            String reason = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(player.getName()).getReason();
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Ban.constructBanMessage(reason, Ban.getBanID(player)));
        }
    }
}
