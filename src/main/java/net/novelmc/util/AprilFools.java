package net.novelmc.util;

import net.novelmc.ConversePlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.Random;


public class AprilFools implements Listener {
    private static boolean fooler;
    private Random random = new Random();
    private int bound = 10;

    public AprilFools(ConversePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void start() {
        fooler = true;
    }

    public static void stop() {
        fooler = false;
    }

    /*
    @EventHandler
    public void playerLoginEvent(PlayerLoginEvent event) {
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
                int x = random.nextInt(3);
                if (x == 1) {
                    player.setVelocity(new Vector(0,100,0));
                    player.setGameMode(GameMode.SURVIVAL);
                }
                if (x == 2) {
                    for (int xloc = 0; xloc <= 10; xloc++) {
                        for (int zloc = 0; zloc <= 10; zloc++) {
                            world.strikeLightning(location.clone().add(xloc, 0, zloc));
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
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (fooler) {
            Player player = event.getPlayer();
            Location location = player.getLocation();
            World world = player.getWorld();
            int xLoc = location.getBlockX();
            int yLoc = location.getBlockY() - 1;
            int zLoc = location.getBlockZ();
            Block block = world.getBlockAt(xLoc, yLoc, zLoc);
            block.setType(Material.LAVA);

            for (int x = 0; x <= 5; x++)
                for (int z = 0; z <= 5; z++)
                    player.spawnParticle(Particle.ENCHANTMENT_TABLE, location, (x * z) + 1);

        }
    }
    /*

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        if (fooler) {
            for (int count = 0; count <= 50; count++) {
                e.getPlayer().getWorld().spawnEntity(e.getPlayer().getEyeLocation(), EntityType.CREEPER);
            }
            e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation(), 5);

        }
    }

     */
}
