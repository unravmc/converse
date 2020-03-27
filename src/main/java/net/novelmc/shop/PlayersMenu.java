package net.novelmc.shop;

import net.novelmc.Converse;
import net.novelmc.util.ShopIndex;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayersMenu extends ShopIndex {

    public PlayersMenu() {
        super(54, "Players Menu");


        Bukkit.getOnlinePlayers().forEach(target -> {
            AtomicInteger x = new AtomicInteger();
            if (Util.size() > 54) {
                do {
                    ItemStack is = newPlayerHead(target);
                    setItem(x.get(), is, player -> player.sendMessage("You clicked on " + target.getName()));
                    x.getAndIncrement();
                } while (x.get() <= 52);
                AtomicInteger y = new AtomicInteger();
                SecondMenu second = new SecondMenu();
                ItemStack is2 = newItem(Material.RED_WOOL, "Previous", "Return to the previous page.");
                ItemStack is3 = newItem(Material.GREEN_WOOL, "Next", "Go to the next page.");
                setItem(53, is3, player -> {
                    do {
                        ItemStack is = newPlayerHead(target);
                        second.setItem(y.get(), is, p -> player.sendMessage("You clicked on " + target.getName()));
                        y.getAndIncrement();
                    } while (x.get() < Util.size());
                    second.setItem(Util.size() + 2, is2, p -> {
                        Converse.plugin.players.open(p);
                    });
                    second.open(player);
                });
            }
            ItemStack is = newPlayerHead(target);
            setItem(x.get(), is, player -> player.sendMessage("You clicked on " + target.getName()));
            x.getAndIncrement();
        });
    }

    private class SecondMenu extends ShopIndex {
        public SecondMenu() {
            super(54, "Players Menu - Page 2");
        }
    }
}

