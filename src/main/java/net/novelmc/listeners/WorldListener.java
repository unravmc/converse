package net.novelmc.listeners;

import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WorldListener implements Listener {
    private Converse plugin;

    public WorldListener(Converse plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("multiverse.access.staffworld") &&
                !plugin.lp.isStaff(event.getPlayer().getUniqueId()) ||
                !plugin.lp.isArchitect(event.getPlayer().getUniqueId())) {
            plugin.lp.disallowStaffWorld(event.getPlayer().getUniqueId());
        }
    }

    private boolean bool = Converse.plugin.config.getBoolean("item_drops");

    @EventHandler
    public void PlayerDrops(PlayerDropItemEvent e) {
        e.setCancelled(!bool);
    }

    @EventHandler
    public void ItemSpawn(ItemSpawnEvent e) {
        e.setCancelled(!bool);
    }

    @EventHandler
    public void BlockDrops(BlockDropItemEvent e) {
        e.setCancelled(!bool);
        Player p = e.getPlayer();
        GameMode gm = p.getGameMode();
        if (gm.equals(GameMode.SURVIVAL)) {
            ItemStack is;
            List<Item> items = e.getItems();
            for (Item item : items) {
                is = item.getItemStack();
                assign(p, is);
            }
        }
    }

    @EventHandler
    public void EntityDrops(EntityDropItemEvent e) {
        e.setCancelled(!bool);
    }

    @EventHandler
    public void BlockPlacement(BlockPlaceEvent event) {
        Material m = event.getBlockPlaced().getType();
        if (Converse.plugin.config.getBoolean("fluid_place") == false) {
            if (mats().contains(m)) {
                event.setCancelled(true);
            }
        }

        if (Converse.plugin.config.getBoolean("fire_place") == false) {
            if (m == Material.FIRE) {
                event.setCancelled(true);
            } else if (m == Material.FIRE_CHARGE) {
                event.setCancelled(true);
            }
        }
    }

    private void assign(Player player, ItemStack... itemStack) {
        player.getInventory().addItem(itemStack);
    }

    private ArrayList<Material> mats() {
        ArrayList<Material> materials = new ArrayList<>();
        materials.add(Material.LAVA);
        materials.add(Material.LAVA_BUCKET);
        materials.add(Material.WATER_BUCKET);
        materials.add(Material.WATER);
        return materials;
    }
}
