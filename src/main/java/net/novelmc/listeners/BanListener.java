package net.novelmc.listeners;

import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BanListener implements Listener {
    private final Converse plugin;

    @SuppressWarnings("")
    public BanListener(Converse plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (plugin.permban.isBanned(player)) {
            if (player.hasPermission("converse.ban.bypass")) {
                plugin.permban.removePermban(player);
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.permban
                    .constructBanMessage(plugin.permban.getReason(player), plugin.permban.getBanID(player)));
        }

        if (plugin.ban.isBanned(player)) {
            if (player.hasPermission("converse.ban.bypass")) {
                plugin.ban.removeBan(player);
                return;
            }
            plugin.ban.removeBan(player);
        } else {
            if (plugin.ban.getPlayer(player)) {
                if (player.hasPermission("converse.ban.bypass")) {
                    plugin.ban.removeBan(player);
                    return;
                }
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.ban
                        .constructBanMessage(player, plugin.ban.getReason(player), plugin.ban.getBanID(player)));
            }
        }
    }
}
