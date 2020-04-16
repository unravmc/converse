package net.novelmc.listeners;

import net.novelmc.ConversePlugin;
import net.novelmc.util.ShopIndex;
import net.novelmc.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldListener implements Listener {
    private ConversePlugin plugin;

    public WorldListener(ConversePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Custom Join Manage
        Player player = event.getPlayer();
        ChatColor rankColor = ConversePlugin.plugin.lp.displayRankColor(player);
        ChatColor nameColor = ConversePlugin.plugin.lp.nameColor(player);
        String rank = ConversePlugin.plugin.lp.displayRank(player);
        StringBuilder sb = new StringBuilder();
        if (ConversePlugin.plugin.lp.isStaff(player.getUniqueId())
                || ConversePlugin.plugin.lp.isArchitect(player.getUniqueId())
                || ConversePlugin.plugin.lp.isVoter(player.getUniqueId())) {
                sb.append(ChatColor.DARK_GRAY + "[")
                        .append(ChatColor.GREEN + "+")
                        .append(ChatColor.DARK_GRAY + "] ")
                        .append("[" + rankColor + rank + ChatColor.DARK_GRAY + "] ")
                        .append(nameColor + player.getName());
        } else {
            sb.append(ChatColor.DARK_GRAY + "[")
                    .append(ChatColor.GREEN + "+")
                    .append(ChatColor.DARK_GRAY + "] ")
                    .append(nameColor + player.getName());
        }
        event.setJoinMessage(sb.toString());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("multiverse.access.staffworld") &&
                !plugin.lp.isStaff(event.getPlayer().getUniqueId()) ||
                !plugin.lp.isArchitect(event.getPlayer().getUniqueId())) {
            plugin.lp.disallowStaffWorld(event.getPlayer().getUniqueId());
        }
        UUID playerUUID = event.getPlayer().getUniqueId();
        ShopIndex.openInventories.remove(playerUUID);

        //custom leave
        Player player = event.getPlayer();
        ChatColor nameColor = ConversePlugin.plugin.lp.nameColor(player);
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.DARK_GRAY + "[")
                .append(ChatColor.RED + "-")
                .append(ChatColor.DARK_GRAY + "] ")
                .append(nameColor + player.getName());
        event.setQuitMessage(sb.toString());
    }

    public boolean isDropsAllowed = ConversePlugin.plugin.config.getBoolean("item_drops");

    @EventHandler
    public void PlayerDrops(PlayerDropItemEvent e) {
        e.setCancelled(!isDropsAllowed);
    }

    @EventHandler
    public void ItemSpawn(ItemSpawnEvent e) {
        e.setCancelled(!isDropsAllowed);
    }

    @EventHandler
    public void BlockDrops(BlockDropItemEvent e) {
        e.setCancelled(!isDropsAllowed);
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
        e.setCancelled(!isDropsAllowed);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Util.isInOrbit(player.getUniqueId())) {
            player.setVelocity(new Vector(0,10,0));
        }

        if (Util.map.containsKey(player)) {
            for (int x = 0; x <= 5; x++)
                for (int z = 0; z <= 5; z++)
                    player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), (x * z) + 1);
        }
    }

    @EventHandler
    public void BlockPlacement(BlockPlaceEvent event) {
        Material m = event.getBlockPlaced().getType();
        if (ConversePlugin.plugin.config.getBoolean("fluid_place") == false) {
            if (mats().contains(m)) {
                event.setCancelled(true);
            }
        }

        if (ConversePlugin.plugin.config.getBoolean("fire_place") == false) {
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
