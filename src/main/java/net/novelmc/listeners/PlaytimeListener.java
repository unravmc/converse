package net.novelmc.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.novelmc.Converse;
import net.novelmc.commands.CageCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlaytimeListener implements Listener {
    public BukkitTask schedular;
    public Map<UUID, Long> playtime = new HashMap<>();
    public Map<UUID, Long> timeLoggedIn = new HashMap<>();
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Converse plugin;
    public final Map<UUID, CageCommand.Cage> cages = new HashMap<>();

    public PlaytimeListener(Converse plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        if(new File(plugin.getDataFolder(), "playtime.json").exists()) loadData();
        beginScheduler();
    }

    private void beginScheduler() {
        schedular = new BukkitRunnable() {
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    cachePlayerPlaytime(player);
                }

                saveData();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 6000L);
    }

    public void saveData() {
        try {
            FileWriter fw = new FileWriter(new File(plugin.getDataFolder(), "playtime.json"));
            gson.toJson(playtime, fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            FileReader fr = new FileReader(new File(plugin.getDataFolder(), "playtime.json"));
            Type type = new TypeToken<Map<UUID, Long>>() { }.getType();
            playtime = gson.fromJson(fr, type);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cachePlayerPlaytime(Player player) {
        Long loggedInAt = timeLoggedIn.get(player.getUniqueId());
        Long currentTimeMillis = System.currentTimeMillis();
        Long diffTime = currentTimeMillis - loggedInAt;
        timeLoggedIn.put(player.getUniqueId(), currentTimeMillis);
        playtime.put(player.getUniqueId(), playtime.get(player.getUniqueId()) + diffTime);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        timeLoggedIn.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        if(!playtime.containsKey(e.getPlayer().getUniqueId())) {
            playtime.put(e.getPlayer().getUniqueId(), 0L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        cachePlayerPlaytime(e.getPlayer());
    }
}
