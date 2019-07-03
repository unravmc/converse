package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.bridge.LuckPermsBridge;
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

    private void changeTabColor(Player player)
    {
        UUID uuid = player.getUniqueId();
        if (LuckPermsBridge.isStaff(uuid))
        {
            player.setPlayerListName(LuckPermsBridge.displayRankColor(player) + player.getName());
        }
    }
}
