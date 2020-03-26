package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayersMenu extends ShopIndex {

    public PlayersMenu() {
        super(Bukkit.getOnlinePlayers().size() + 1, "Players Menu");

        AtomicInteger x = new AtomicInteger();
        Bukkit.getOnlinePlayers().forEach(target -> {
            ItemStack is = newPlayerHead(target);
            setItem(x.get(), is, player -> player.sendMessage("You clicked on " + target.getName()));
            x.getAndIncrement();
        });
    }
}