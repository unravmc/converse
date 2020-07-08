package net.novelmc.util;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Recurrent extends BukkitRunnable {
    private final Plugin plugin;
    private final Server server;

    private static Long lastRan = null;

    public static Long getLastRan() {
        return lastRan;
    }

    public Recurrent(Plugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
    }

    @Override
    public void run() {
        lastRan = System.currentTimeMillis();
    }
}
