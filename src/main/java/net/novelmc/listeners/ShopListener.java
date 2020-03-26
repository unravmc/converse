package net.novelmc.listeners;

import net.novelmc.util.ShopIndex;

import java.util.UUID;

import net.novelmc.Converse;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ShopListener implements Listener {

    public ShopListener(Converse plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        UUID invUUID = ShopIndex.openInventories.get(playerUUID);
        if (invUUID != null) {
            e.setCancelled(true);
            ShopIndex gui = ShopIndex.getInvByUUID().get(invUUID);
            ShopIndex.GUIAction action = gui.getActions().get(e.getSlot());
            if (action != null) {
                action.click(player);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ShopIndex.openInventories.remove(playerUUID);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ShopIndex.openInventories.remove(playerUUID);
    }
}