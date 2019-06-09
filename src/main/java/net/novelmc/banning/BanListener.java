package net.novelmc.banning;

import net.novelmc.Converse;
import net.novelmc.util.Ips;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BanListener implements Listener
{

    // cleanup
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        if (Converse.bans.contains(Ips.getIp(event).replace(".", "-")) || Converse.bans.contains(player.getName().replace("username: ", "")))
        {
            if (Converse.bans.contains(Ips.getIp(event).replace(".", "-") + ".reason"))
            {
                if (Converse.bans.getBoolean("canappeal"))
                {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are banned!" + "\nBanned by: " + Converse.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by") + "\nReason: " + ChatColor.YELLOW + Converse.bans.getString(Ips.getIp(event).replace(".", "-") + ".reason") + ChatColor.RED + "\nYou can appeal at " + Converse.config.getString("ban-url"));

                }
                if (!Converse.bans.getBoolean("canappeal"))
                {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are banned!" + "\nBanned by: " + Converse.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by") + "\nReason: " + ChatColor.YELLOW + Converse.bans.getString(Ips.getIp(event).replace(".", "-") + ".reason"));
                }
            }
            if (!Converse.bans.contains(Ips.getIp(event).replace(".", "-") + ".reason"))
            {
                if (Converse.bans.getBoolean("canappeal"))
                {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are banned!" + "\nBanned by: " + Converse.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by") + "\nYou can appeal at " + Converse.config.getString("ban-url"));
                }
                if (!Converse.bans.getBoolean("canappeal"))
                {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are banned!" + "\nBanned by: " + Converse.bans.getString(Ips.getIp(event).replace(".", "-") + ".banned-by"));
                }
            }
        }
    }
}
