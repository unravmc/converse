package net.novelmc.listeners;

import java.util.UUID;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ModeListener implements Listener
{
    private Converse plugin = Converse.plugin;

    public static void enableEventMode()
    {
        Converse.plugin.getConfig().set("mode", "event");
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.setWhitelisted(true);
        }
        Util.action("The server has entered event mode, all online players have been whitelisted.");
    }

    public static void disableEventMode()
    {
        Converse.plugin.getConfig().set("mode", "default");
        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            player.setWhitelisted(false);
        }
        Util.action("The server has left event mode.");
    }

    public static void enableDevMode()
    {
        Converse.plugin.getConfig().set("mode", "dev");
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!LuckPermsBridge.isDeveloper(player.getUniqueId()) && !LuckPermsBridge.isExecutive(player.getUniqueId()))
            {
                player.kickPlayer("The server has entered developer only mode.");
            }
        }
        Util.action("The server has entered developer-mode.");
    }

    public static void disableDevMode()
    {
        Converse.plugin.getConfig().set("mode", "default");
        Util.action("The server has left developer-only mode.");
    }

    public static void enableStaffMode()
    {
        Converse.plugin.getConfig().set("mode", "staff");
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!LuckPermsBridge.isStaff(player.getUniqueId()))
            {
                player.kickPlayer("The server has entered staff-only mode.");
            }
        }
        Util.action("The server has entered staff-only mode.");
    }

    public static void disableStaffMode()
    {
        Converse.plugin.getConfig().set("mode", "default");
        Util.action("The server has left staff-only mode.");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();

        // Event Mode
        if (plugin.getConfig().getString("mode").equalsIgnoreCase("event")
                && !player.isWhitelisted()
                && !LuckPermsBridge.isStaff(uuid))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in event mode.");
        }

        // Developer Mode
        if (plugin.getConfig().getString("mode").equalsIgnoreCase("dev"))
        {
            if (!LuckPermsBridge.isDeveloper(uuid) || !LuckPermsBridge.isExecutive(uuid))
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in developer only mode.");
            }
        }

        // Staff Mode
        if (plugin.getConfig().getString("mode").equalsIgnoreCase("staff") && !LuckPermsBridge.isStaff(uuid))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "&cThe server is currently in staff-only mode.");
        }
    }
}
