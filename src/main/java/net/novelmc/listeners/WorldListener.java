package net.novelmc.listeners;

import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class WorldListener extends ConverseBase implements Listener
{
    private static LuckPermsApi api = Converse.getLuckPermsAPI();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (event.getPlayer().hasPermission("multiverse.access.staffworld") &&
                !plugin.lp.isStaff(event.getPlayer().getUniqueId()) ||
                !plugin.lp.isArchitect(event.getPlayer().getUniqueId()))
        {
            plugin.lp.disallowStaffWorld(event.getPlayer().getUniqueId());
        }
    }
}
