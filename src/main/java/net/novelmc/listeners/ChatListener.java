package net.novelmc.listeners;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    private static List<Player> punished = new ArrayList<>();

    public static boolean isPunished(Player player)
    {
        return punished.contains(player);
    }

    public static void setPunished(Player player, boolean mute)
    {
        if (mute)
        {
            punished.add(player);
            return;
        }
        punished.remove(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (isPunished(event.getPlayer()))
        {
            String message = event.getMessage();
            event.setMessage(ChatColor.DARK_GRAY + message);
        }
    }
}
