package net.novelmc.util.nbt;

import net.novelmc.ConversePlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class NBTListener implements Listener {
    private Plugin plugin;

    public NBTListener(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInvClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
        final Player p = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        if (ConversePlugin.fixer.isHackedItem(event.getCurrentItem(), p)) {
            event.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onDrop(PlayerDropItemEvent event) {
        final Player p = event.getPlayer();
        assert p != null;
        if (event.getItemDrop() == null) return;
        if (ConversePlugin.fixer.isHackedItem(event.getItemDrop().getItemStack(), p)) {
            event.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void slotChange(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        assert p != null;
        ItemStack stack = p.getInventory().getItem(event.getNewSlot());
        if (ConversePlugin.fixer.isHackedItem(stack, p)) {
            event.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (ItemStack stack : event.getPlayer().getInventory().getContents()) {
            if (ConversePlugin.fixer.isHackedItem(stack, event.getPlayer())) {
                event.getPlayer().getInventory().remove(stack);
                event.getPlayer().updateInventory();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlibListener.c.invalidate(event.getPlayer());
    }
}
