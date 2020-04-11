package net.novelmc.listeners;

import net.novelmc.Converse;
import net.novelmc.playerdata.PlayerData;
import net.novelmc.util.ConverseBase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@SuppressWarnings("unused")
public class ManageListener extends ConverseBase implements Listener {
    public ManageListener(Converse plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent e) {
        if (plugin.playerDataManager.getPlayerData(e.getPlayer()).getManagedSettings().isMovementDisallowed()) {
            Location to = e.getTo();
            Location from = e.getFrom();
            if (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) {
                e.setTo(from);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        PlayerData pData = plugin.playerDataManager.getPlayerData(p);
        if (pData.getManagedSettings().isMovementDisallowed()) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onProcessCommand(PlayerCommandPreprocessEvent e) {
        if (plugin.playerDataManager.getPlayerData(e.getPlayer()).getManagedSettings().isCommandsDisallowed()) {
            e.getPlayer().sendMessage(ChatColor.RED + "Your commands are disabled!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (plugin.playerDataManager.getPlayerData(e.getPlayer()).getManagedSettings().isBlockeditDisallowed()) {
            e.getPlayer().sendMessage(ChatColor.RED + "Your block editing permissions are disabled!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        if (plugin.playerDataManager.getPlayerData(e.getPlayer()).getManagedSettings().isBlockeditDisallowed()) {
            e.getPlayer().sendMessage(ChatColor.RED + "Your block editing permissions are disabled!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (plugin.playerDataManager.getPlayerData(((Player) e.getDamager())).getManagedSettings().isPVPDisallowed()) {
                e.getDamager().sendMessage(ChatColor.RED + "Your damage permissions are disabled!");
                e.setCancelled(true);
            }
        }
    }

}
