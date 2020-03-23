package net.novelmc.shop;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class PlayersMenu extends ShopIndex {
    
    public PlayersMenu() {
        super(playerCount + 1, "Players Menu");
        Bukkit.getOnlinePlayers().forEach((p) -> {
            ItemStack is = newPlayerHead(p);
            int x = 0;
            do {
                setItem(x, is, player -> {
                    player.sendMessage("Thanks for testing!!");
                });
                x++;
            } while (x <= playerCount);
        });
    }
}