package net.novelmc.shop;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayersMenu extends ShopIndex {
    
    public PlayersMenu() {
        super(playerCount + 1, "Players Menu");
        Iterator it = Bukkit.getOnlinePlayers().iterator();
        
        it.forEachRemaining((target) -> {
            Player p = (Player) target;
            ItemStack is = newPlayerHead(p);
            int x = 0;
            do {
                setItem(x, is, player -> {
                    player.sendMessage("You clicked on " + p.getName());
                });
                x++;
            } while (it.hasNext());
        });
    }
}