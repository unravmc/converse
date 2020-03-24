package net.novelmc.listeners;

import java.util.ArrayList;
import java.util.List;
import net.novelmc.Converse;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    private final Converse plugin;
    private Util util = new Util();

    @SuppressWarnings("")
    public ChatListener(Converse plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private static final List<Player> punished = new ArrayList<>();

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
        String message = event.getMessage();
        Player player = event.getPlayer();
        
        if (util.isInAdminChat(player)) {
            event.setCancelled(true);
            util.adminchat(player, message);
        }
        
        if (isPunished(player))
        {
            event.setMessage(ChatColor.BLACK + message);
        }
    }
}
