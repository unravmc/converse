package net.novelmc.listeners;

import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class WorldListener implements Listener
{
    private static LuckPermsApi api = Converse.getLuckPermsAPI();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (event.getPlayer().hasPermission("multiverse.access.staffworld") &&
                !LuckPermsBridge.isStaff(event.getPlayer().getUniqueId()) ||
                !LuckPermsBridge.isArchitect(event.getPlayer().getUniqueId()))
        {
            LuckPermsBridge.disallowStaffWorld(event.getPlayer().getUniqueId());
        }
    }
}
