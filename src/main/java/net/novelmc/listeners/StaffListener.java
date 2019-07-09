package net.novelmc.listeners;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.node.NodeAddEvent;
import me.lucko.luckperms.api.event.node.NodeMutateEvent;
import me.lucko.luckperms.api.event.player.PlayerDataSaveEvent;
import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StaffListener extends ConverseBase implements Listener
{
    private Converse plugin;

    public StaffListener(Converse plugin)
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
        player.setPlayerListName(plugin.lp.displayRank(player) + ChatColor.RESET + " " + player.getName());
    }

    @EventHandler
    private void onGroupChange(NodeMutateEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player player = Bukkit.getPlayer(event.getTarget().getFriendlyName());
            if (player != null)
            {
                player.setPlayerListName(plugin.lp.displayRank(player) + ChatColor.RESET + " " + player.getName());
            }
        });
    }
}
