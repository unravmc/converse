package net.novelmc.listeners;

import net.novelmc.ConversePlugin;
import net.novelmc.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlaytimeListener implements Listener {
    public BukkitTask scheduler;
    public Map<UUID, Long> timeLoggedIn = new HashMap<>();
    private final ConversePlugin plugin;

    public PlaytimeListener(ConversePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        beginScheduler();
    }

    private void beginScheduler() {
        scheduler = new BukkitRunnable() {
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    cachePlayerPlaytime(player);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 6000L);
    }

    public void cachePlayerPlaytime(Player player) {
        Long loggedInAt = timeLoggedIn.get(player.getUniqueId());
        Long currentTimeMillis = System.currentTimeMillis();
        long diffTime = currentTimeMillis - loggedInAt;
        timeLoggedIn.put(player.getUniqueId(), currentTimeMillis);
        PlayerData pData = plugin.playerDataManager.getPlayerData(player);
        pData.setPlaytime(pData.getPlaytime() + diffTime);
        plugin.playerDataManager.save(pData);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        timeLoggedIn.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        cachePlayerPlaytime(e.getPlayer());
    }
}
