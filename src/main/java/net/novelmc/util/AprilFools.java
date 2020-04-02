package net.novelmc.util;

import net.novelmc.Converse;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;


public class AprilFools implements Listener {
    private static boolean fooler;
    private Random random = new Random();
    private int bound = 10;

    public AprilFools(Converse plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void start() { fooler = true; }

    public static void stop() {
        fooler = false;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        if (fooler) {
            event.getPlayer().setDisplayName("Cumbaby");
            event.getPlayer().setCustomName("Cumbaby");
            event.getPlayer().setCustomNameVisible(true);
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (fooler) {
            e.setDeathMessage(e.getEntity().getPlayer().getName() + " tried to eat too much ass!");
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        if (fooler) {
            e.getBlock().setType(Material.BEDROCK);
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        if (fooler) {
            e.getPlayer().getInventory().getItemInMainHand().setType(Material.BEETROOT);
        }
    }

    @EventHandler
    public void playerInteractEvent (PlayerInteractEvent e) {
        if (fooler) {

            Action action = e.getAction();
            Player player = e.getPlayer();
            Block block = e.getClickedBlock();
            ItemStack is = player.getInventory().getItemInMainHand();
            World world = player.getWorld();
            Location location = player.getLocation();
            if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_AIR)) {
                int x = random.nextInt(2);
                if (x == 1) {
                    player.setVelocity(new Vector(0,100,0));
                    player.setGameMode(GameMode.SURVIVAL);
                }
                if (x == 2) {
                    for (int xloc = 0; xloc <= 10; xloc++) {
                        for (int zloc = 0; zloc <= 10; zloc++) {
                            world.strikeLightning(location);
                        }
                    }
                }
            }

            if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                boolean glow = false;
                if (!glow) {
                    glow = true;
                    player.setGlowing(true);
                } else {
                    glow = false;
                    player.setGlowing(false);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (fooler) {
            int x = random.nextInt(bound);
            if (x > 15 || x < 20) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        if (fooler) {
            for (int count = 0; count <= 50; count++) {
                e.getPlayer().getWorld().spawnEntity(e.getPlayer().getEyeLocation(), EntityType.CREEPER);
            }
            e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation(), 5);

        }
    }
}
