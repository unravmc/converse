package net.novelmc.listeners;

import java.util.ArrayList;
import java.util.List;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener implements Listener
{
    private static List<Player> muted = new ArrayList<>();

    public static boolean isMuted(Player player)
    {
        return muted.contains(player);
    }

    public static void setMuted(Player player, boolean mute)
    {
        if (mute)
        {
            muted.add(player);
            return;
        }
        muted.remove(player);
    }

    public static int getMutedAmount()
    {
        return muted.size();
    }

    public static void purgeMuted()
    {
        muted.clear();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        if (LuckPermsBridge.isStaff(player.getUniqueId()))
        {
            return;
        }
        if (isMuted(player))
        {
            player.sendMessage(ChatColor.RED + "You are currently muted and cannot chat.");
            e.setCancelled(true);
        }
    }
}
