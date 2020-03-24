package net.novelmc.shop;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayersMenu extends ShopIndex {
    
    public PlayersMenu() {
        super(Bukkit.getOnlinePlayers().size() + 1, "Players Menu");
        
        int x = 0;
        for (Player target : Bukkit.getOnlinePlayers())
        {
            ItemStack is = newPlayerHead(target);
            setItem(x, is, player -> {
                player.sendMessage("You clicked on " + target.getName());
            });
            x++;
        }
    }
}