package net.novelmc.listeners;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.tablist.TitledTabList;
import java.util.List;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.node.NodeMutateEvent;
import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListener extends ConverseBase implements Listener
{
    private Converse plugin;

    public TabListener(Converse plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(NodeMutateEvent.class, this::onGroupChange);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        try
        {
            Tabbed tabbed = new Tabbed(plugin);
            TitledTabList tab = tabbed.newTitledTabList(player);
            List<String> header = plugin.config.getStringList("tablist.header");
            List<String> footer = plugin.config.getStringList("tablist.footer");
            tab.setHeader(Util.colorize(StringUtils.join(header, "\n")));
            tab.setFooter(Util.colorize(StringUtils.join(footer, "\n")));
            
        }
        catch (NoClassDefFoundError ignored)
        {
        }
        String rank = plugin.lp.displayRank(player);
        ChatColor color = plugin.lp.displayRankColor(player);
        
        if (!plugin.lp.isStaff(player.getUniqueId())) 
        {
            player.setPlayerListName(plugin.lp.nameColor(player) + player.getName());
        } 
        else 
        { player.setPlayerListName(ChatColor.DARK_GRAY + "[" 
                + color + rank 
                + ChatColor.DARK_GRAY + "]" 
                + plugin.lp.nameColor(player) + " " + player.getName()); 
        }
        
        Util.tabAdd(player);
    }

    @EventHandler
    private void onGroupChange(NodeMutateEvent event)
    {
        Bukkit.getScheduler().runTask(plugin, () ->
        {
            Player player = Bukkit.getPlayer(event.getTarget().getFriendlyName());
            String rank = plugin.lp.displayRank(player);
            ChatColor color = plugin.lp.displayRankColor(player);
            if (player == null) {
                throw new NullPointerException();
            }
            
            if (plugin.lp.isStaff(player.getUniqueId()))
            {
                player.setPlayerListName(ChatColor.DARK_GRAY + "[" 
                + color + rank 
                + ChatColor.DARK_GRAY + "]" 
                + plugin.lp.nameColor(player) + " " + player.getName());
            } else {
                player.setPlayerListName(plugin.lp.nameColor(player) + player.getName());
            }
            
            Util.tabRemove(player);
            Util.tabAdd(player);
        });
    }
}
