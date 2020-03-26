package net.novelmc.listeners;

import java.util.ArrayList;
import java.util.List;

import net.novelmc.Converse;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener {
    public Util util = Converse.util;

    @SuppressWarnings("")
    public ChatListener(Converse plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private static final List<Player> punished = new ArrayList<>();

    public static boolean isPunished(Player player) {
        return punished.contains(player);
    }

    public static void setPunished(Player player, boolean mute) {
        if (mute) {
            punished.add(player);
            return;
        }
        punished.remove(player);
    }

    @EventHandler
    public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        if (isPunished(player)) {
            event.setMessage(ChatColor.BLACK + message);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void adminChat(@NotNull AsyncPlayerChatEvent event) {
        String message = event.getMessage().trim();
        Player p = event.getPlayer();

        if (util.isInAdminChat(p.getUniqueId())) {
            event.setCancelled(true);
            util.adminchat(p, message);
        }
    }
}
