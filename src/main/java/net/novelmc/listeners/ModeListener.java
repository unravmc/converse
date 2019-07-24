package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.Converse;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class ModeListener implements Listener
{
    private final Converse plugin;

    @SuppressWarnings("")
    public ModeListener(Converse plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void enableEventMode()
    {
        plugin.config.set("mode", "event");
        Bukkit.getOnlinePlayers().forEach((player) ->
        {
            player.setWhitelisted(true);
        });
        Util.action("The server has entered event mode, all online players have been whitelisted.");
    }

    public void disableEventMode()
    {
        plugin.config.set("mode", "default");
        Bukkit.getWhitelistedPlayers().forEach((player) ->
        {
            player.setWhitelisted(false);
        });
        Util.action("The server has left event mode.");
    }

    public void enableDevMode()
    {
        plugin.config.set("mode", "dev");
        Bukkit.getOnlinePlayers().stream().filter((player) -> (!plugin.lp.isDeveloper(player.getUniqueId()) && !plugin.lp.isExecutive(player.getUniqueId()))).forEachOrdered((player) ->
        {
            player.kickPlayer("The server has entered developer only mode.");
        });
        Util.action("The server has entered developer-mode.");
    }

    public void disableDevMode()
    {
        plugin.config.set("mode", "default");
        Util.action("The server has left developer-only mode.");
    }

    public void enableStaffMode()
    {
        plugin.config.set("mode", "staff");
        Bukkit.getOnlinePlayers().stream().filter((player) -> (!plugin.lp.isStaff(player.getUniqueId()))).forEachOrdered((player) ->
        {
            player.kickPlayer("The server has entered staff-only mode.");
        });
        Util.action("The server has entered staff-only mode.");
    }

    public void disableStaffMode()
    {
        plugin.config.set("mode", "default");
        Util.action("The server has left staff-only mode.");
    }

    @SuppressWarnings("")
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();

        // Event Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("event")
                && !player.isWhitelisted()
                && !plugin.lp.isStaff(uuid))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in event mode.");
        }

        // Developer Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("dev"))
        {
            if (!plugin.lp.isDeveloper(uuid) || !plugin.lp.isExecutive(uuid))
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in developer only mode.");
            }
        }

        // Staff Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("staff") && !plugin.lp.isStaff(uuid))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in staff-only mode.");
        }
    }

    @SuppressWarnings("")
    @EventHandler
    public void onServerPing(ServerListPingEvent event)
    {
        if (plugin.config.getString("mode").equalsIgnoreCase("default"))
        {
            return;
        }

        // Event Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("event"))
        {
            event.setMotd(ChatColor.RED + "The server is currently in event mode.");
            return;
        }

        // Developer Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("dev"))
        {
            event.setMotd(ChatColor.RED + "The server is currently in development mode.");
            return;
        }

        // Staff Mode
        if (plugin.config.getString("mode").equalsIgnoreCase("staff"))
        {
            event.setMotd(ChatColor.RED + "The server is currently in staff mode.");
        }
    }
}
