package net.novelmc.util;

import net.novelmc.ConversePlugin;
import net.novelmc.util.nbt.MalformedStateException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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

        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            Chunk c = p.getLocation().getChunk();
            try {
                ConversePlugin.fixer.checkChunk(c);
            } catch (MalformedStateException ex) {
                Bukkit.getLogger().severe(ExceptionUtils.getStackTrace(ex));
            }
        });
    }
}
