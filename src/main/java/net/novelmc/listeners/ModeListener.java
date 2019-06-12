package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ModeListener implements Listener
{
    private Converse plugin = Converse.plugin;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();

        // Event Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("event")
                && !player.isWhitelisted()
                && !LuckPermsBridge.isStaff(uuid))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in event mode.");
        }

        // Developer Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("dev"))
        {
            if (!LuckPermsBridge.isDeveloper(uuid) || !LuckPermsBridge.isExecutive(uuid))
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in developer only mode.");
            }
        }

        // Staff Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("staff") && !LuckPermsBridge.isStaff(uuid))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "&cThe server is currently in staff-only mode.");
        }
    }
}
