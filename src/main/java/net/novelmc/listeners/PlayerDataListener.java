package net.novelmc.listeners;

import net.novelmc.Converse;
import net.novelmc.util.ShopIndex;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDataListener implements Listener
{
    private final Converse plugin;

    @SuppressWarnings("")
    public PlayerDataListener(Converse plugin)
    {
        
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        //initialize playerdata
        plugin.pd.createPlayer(event.getPlayer());
        
        //custom login
        Player p = event.getPlayer();
        String jpref = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] ";
        
        if (!plugin.lp.isStaff(p.getUniqueId())) {
            event.setJoinMessage(jpref + ChatColor.GRAY + p.getName());
        }
        else {
            String rank = plugin.lp.displayRank(p);
            ChatColor color = plugin.lp.displayRankColor(p);
            event.setJoinMessage(jpref + ChatColor.DARK_GRAY + "[" 
                + color + rank 
                + ChatColor.DARK_GRAY + "] " 
                + plugin.lp.nameColor(p) + p.getName());
        }
    }
    
    //custom leave.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void customLeaveMessage(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        String rank = plugin.lp.displayRank(p);
        ChatColor color2 = plugin.lp.displayRankColor(p);
        ChatColor color = plugin.lp.nameColor(p);
        String jpref = ChatColor.DARK_GRAY + "[" 
                + ChatColor.RED + "-" 
                + ChatColor.DARK_GRAY + "] ";
        if (!plugin.lp.isStaff(p.getUniqueId())) {
            event.setQuitMessage(jpref + ChatColor.GRAY 
            + p.getName());
        } else {
            event.setQuitMessage(jpref 
                    + ChatColor.DARK_GRAY + "[" 
                    + color2 + rank 
                    + ChatColor.DARK_GRAY + "] " 
                    + color + p.getName()); 
        }
    }
}
