package net.novelmc.util;

import net.novelmc.Converse;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
    private int bound = 20;

    public AprilFools(Converse plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void start() {
        fooler = true;
    }

    public static void stop() {
        fooler = false;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        if (fooler) {
            int x = random.nextInt(bound);
            if (x == 12) {
                event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+"
                        + ChatColor.DARK_GRAY + "] " + " ");
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (fooler) {
            int x = random.nextInt(bound);
            if (x == 8) {
                e.setDeathMessage(e.getEntity().getPlayer().getName() + " tried to eat too much ass!");
            }
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        if (fooler) {
            int x = random.nextInt(bound);
            if (x == 20) {
                e.setCancelled(true);
                e.getBlock().setType(Material.BEDROCK);
            }
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        if (fooler) {
            int x = random.nextInt(bound);
            if (x == 15) {
                e.setCancelled(true);
                e.getItemInHand().setType(Material.BEETROOT);
            }
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
                int x = random.nextInt(bound);
                if (x == 8) {
                    e.setCancelled(true);
                }
                if (x == 9) {
                    player.setVelocity(new Vector(0,10,0));
                }
                if (x == 10) {
                    for (int xloc = 0; xloc <= 10; xloc++) {
                        for (int zloc = 0; zloc <= 10; zloc++) {
                            world.strikeLightning(location);
                        }
                    }
                }
            }

            if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                int x = random.nextInt(bound);
                if (x == 2) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        if (fooler) {
            int x = random.nextInt(bound);

            if (x == 13) {
                e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation(), 5);
            }
        }
    }
}
