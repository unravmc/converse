package net.novelmc.util;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

public class Ips
{
    public static String getIp(Player player)
    {
        return player.getAddress().getAddress().getHostAddress().trim();
    }

    public static String getIp(PlayerLoginEvent event)
    {
        return event.getAddress().getHostAddress().trim();
    }

}
