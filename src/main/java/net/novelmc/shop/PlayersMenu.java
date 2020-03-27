package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayersMenu extends ShopIndex {

    public PlayersMenu() {
        super(Util.size(), "Players Menu");

        AtomicInteger x = new AtomicInteger();
        Bukkit.getOnlinePlayers().forEach(target -> {
            ItemStack is = newPlayerHead(target);
            setItem(x.get(), is, player -> player.sendMessage("You clicked on " + target.getName()));
            if (x.get() > 54) {
                return;
            }
            x.getAndIncrement();
        });
    }
}