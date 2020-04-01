package net.novelmc.listeners;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.item.PlayerTabItem;
import com.keenant.tabbed.item.TabItem;
import com.keenant.tabbed.tablist.SimpleTabList;
import com.keenant.tabbed.tablist.TitledTabList;
import com.keenant.tabbed.util.Skins;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabListener extends ConverseBase implements Listener {
    private Converse plugin;
    private Tabbed tabbed;

    public TabListener(Converse plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(NodeMutateEvent.class, this::onGroupChange);
        tabbed = new Tabbed(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = Objects.requireNonNull(event.getPlayer());
        try {
            TitledTabList tab = tabbed.newTitledTabList(player);
            List<String> header = plugin.config.getStringList("tablist.header");
            List<String> footer = plugin.config.getStringList("tablist.footer");
            tab.setHeader(Util.colorize(StringUtils.join(header, "\n")));
            tab.setFooter(Util.colorize(StringUtils.join(footer, "\n")));
        } catch (NoClassDefFoundError ignored) {
        }
        String rank = plugin.lp.displayRank(player);
        ChatColor color = plugin.lp.displayRankColor(player);

        if (plugin.lp.isStaff(player.getUniqueId())) {
            player.setPlayerListName(ChatColor.DARK_GRAY + "["
                    + color + rank
                    + ChatColor.DARK_GRAY + "]"
                    + plugin.lp.nameColor(player) + " " + player.getName());
        } else if (plugin.lp.isArchitect(player.getUniqueId()) || plugin.lp.isVoter(player.getUniqueId())) {
            player.setPlayerListName(ChatColor.DARK_GRAY + "["
                    + color + rank
                    + ChatColor.DARK_GRAY + "]"
                    + plugin.lp.nameColor(player) + " " + player.getName());
        } else {
            player.setPlayerListName(plugin.lp.nameColor(player) + player.getName());
        }

        plugin.po.tabAdd(player);
    }

    private void onGroupChange(NodeMutateEvent event) {
        Bukkit.getScheduler().runTask(plugin, () ->
        {
            Player player = Objects.requireNonNull(Bukkit.getPlayer(event.getTarget().getFriendlyName()));
            String rank = plugin.lp.displayRank(player);
            ChatColor color = plugin.lp.displayRankColor(player);
            if (plugin.lp.isStaff(player.getUniqueId())) {
                player.setPlayerListName(ChatColor.DARK_GRAY + "["
                        + color + rank
                        + ChatColor.DARK_GRAY + "]"
                        + plugin.lp.nameColor(player) + " " + player.getName());
            } else if (plugin.lp.isArchitect(player.getUniqueId()) || plugin.lp.isVoter(player.getUniqueId())) {
                player.setPlayerListName(ChatColor.DARK_GRAY + "["
                        + color + rank
                        + ChatColor.DARK_GRAY + "]"
                        + plugin.lp.nameColor(player) + " " + player.getName());
            } else {
                player.setPlayerListName(plugin.lp.nameColor(player) + player.getName());
            }

            plugin.po.tabRemove(player);
            plugin.po.tabAdd(player);
        });
    }
}
