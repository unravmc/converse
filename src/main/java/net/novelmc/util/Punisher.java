package net.novelmc.util;

import net.novelmc.ConversePlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class Punisher implements Listener {
    private static final Map<Player, String> A = new HashMap<>();
    private static final List<String> B = Arrays.asList("freeze", "dummy", "spoof", "block");

    public Punisher(ConversePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static Random random() {
         return new Random();
    }

    public static Map<Player, String> getA() {
        return A;
    }

    public static List<String> getB() {
        return B;
    }

    public static void addToA(Player key, String value) {
        String v = value;
        if (v.contains(" ")) {
            v.replace(" ", "");
        }
        A.put(key, v);
    }

    public static void removeFromA(Player key) {
        A.remove(key);
    }

    public static List<String> getKeys() {
        List<String> contents = new LinkedList<>();
        if (A.isEmpty() || A == null) {
            return null;
        }
        A.keySet().forEach(p -> {
            contents.add(p.getName());
        });
        return contents;
    }

    public static List<String> getValues() {
        List<String> contents = new LinkedList<>();
        if (A.isEmpty() || A == null) {
            return null;
        }
        A.keySet().forEach(p -> {
            contents.add(A.get(p));
        });
        return contents;
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (A.containsKey(e.getEntity().getPlayer())
                && A.get(e.getEntity().getPlayer().getName()).equalsIgnoreCase("dummy")) {
                e.setDeathMessage(e.getEntity().getPlayer().getName()
                        + " tried to eat too much ass!");
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        Player player = e.getPlayer();

        // Check for a rotation;
        if (from.getWorld() == to.getWorld() && from.distanceSquared(to) < (0.0001 * 0.0001)) {
            return;
        }

        if (A.containsKey(player) && A.get(player).equalsIgnoreCase("freeze")) {
            Location immovable = to.clone();
            immovable.setX(from.getX());
            immovable.setY(from.getY());
            immovable.setZ(from.getZ());
            e.setTo(immovable);
        }

        if (A.containsKey(player) && A.get(player).equalsIgnoreCase("block")) {
            Block block = player.getEyeLocation().getBlock();
            if (block.getType().isSolid()) {
                player.getEyeLocation().getBlock().setType(Material.AIR);
                player.getWorld().spawnFallingBlock(player.getEyeLocation(), block.getBlockData());
            }
            if (block.isLiquid()) {
                player.getEyeLocation().getBlock().setType(Material.BEDROCK);
            }
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        if (A.containsKey(e.getPlayer()) && A.get(e.getPlayer()).equalsIgnoreCase("block")) {
            e.getPlayer().getInventory()
                    .getItemInMainHand().setType(Material.BEETROOT);
            e.getPlayer().updateInventory();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteractEvent (PlayerInteractEvent e) {
        if (A.containsKey(e.getPlayer()) && A.get(e.getPlayer()).equalsIgnoreCase("spoof")) {
            Action action = e.getAction();
            Player player = e.getPlayer();
            Block block = e.getClickedBlock();
            ItemStack is = player.getInventory().getItemInMainHand();
            World world = player.getWorld();
            Location location = player.getLocation();
            if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_AIR)) {
                player.setVelocity(new org.bukkit.util.Vector(0,100,0));
                player.setGameMode(GameMode.SURVIVAL);
            }
            if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                e.getClickedBlock().setType(Material.BEDROCK);
            }
        }
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        if (A.containsKey(e.getPlayer()) && A.get(e.getPlayer()).equalsIgnoreCase("dummy")) {
            e.getPlayer().setHealthScaled(true);
            e.getPlayer().setHealthScale(1.0);
            e.getPlayer().setHealth(1.0);
            e.getPlayer().getInventory().clear();
            e.getPlayer().updateInventory();
            e.getPlayer().chat("Honestly I don't even know why I'm still here.");
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().setFoodLevel(1);
            for (Entity ent : e.getPlayer().getWorld().getEntities()) {
                if (Creature.class.isAssignableFrom(ent.getType().getEntityClass())) {
                    if (e.getPlayer().getLocation().distanceSquared(ent.getLocation()) <= 50.0) {
                        ent.teleport(e.getPlayer().getLocation().clone());
                    }
                }
            }
        }
    }
}
