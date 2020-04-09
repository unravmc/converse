package net.novelmc.playerdata;

import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.Objects;


public class PlayerDataListener extends ConverseBase implements Listener {
    private PlayerDataManager mgr;
    public PlayerDataListener(Converse plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        mgr = plugin.playerDataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(mgr.doesPlayerDataExist(e.getPlayer().getUniqueId())) {
            mgr.cacheExplicitPlayerData(mgr.getPlayerData(e.getPlayer()));
        } else {
            mgr.cacheExplicitPlayerData(new PlayerData(e.getPlayer()));
        }
        mgr.getPlayerData(e.getPlayer()).setLastLoggedIn(new Date());
        mgr.save(mgr.getPlayerData(e.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        mgr.saveAndRemoveCache(mgr.getPlayerData(e.getPlayer()));
    }
}
