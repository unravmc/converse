package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import net.novelmc.Converse;
import net.novelmc.util.HoverIndex;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends ShopIndex {
    public MainMenu() {
        super(9, "UnraveledMC Shop");
        ItemStack is1 = newItem(Material.NETHER_STAR, "Trails", "Use this to get trails with voter coins!");
        ItemStack is2 = newItem(Material.PLAYER_HEAD, "Players", "Use this to open a list of all players.");
        ItemStack is3 = newItem(Material.FEATHER, "Hover Chat", "Hover chat test. Click this to close the gui and print a hoverable text block!");

        setItem(0, is1, player -> Converse.plugin.trails.open(player));

        setItem(1, is2, player -> 
            Bukkit.getOnlinePlayers().forEach(target -> {
                AtomicInteger x = new AtomicInteger();
                if (Util.size() > 54) {
                    do {
                        ItemStack is = newPlayerHead(target);
                        Converse.plugin.players.setItem(x.get(), is, player -> player.sendMessage("You clicked on " + target.getName()));
                        x.getAndIncrement();
                    } while (x.get() <= 52);
                    AtomicInteger y = new AtomicInteger();
                    SecondMenu second = new SecondMenu();
                    ItemStack prev = newItem(Material.RED_WOOL, "Previous", "Return to the previous page.");
                    ItemStack next = newItem(Material.GREEN_WOOL, "Next", "Go to the next page.");
                    setItem(53, next, player -> {
                        do {
                            ItemStack is = newPlayerHead(target);
                            second.setItem(y.get(), is, p -> player.sendMessage("You clicked on " + target.getName()));
                            y.getAndIncrement();
                        } while (x.get() < Util.size());
                        second.setItem(Util.size() + , prev, p -> {
                            Converse.plugin.players.open(p);
                        });
                        second.open(player);
                    });
                }
                
            ItemStack is = newPlayerHead(target);
            setItem(x.get(), is, player -> player.sendMessage("You clicked on " + target.getName()));
            x.getAndIncrement();
        });
    } Converse.plugin.players.open(player));

        setItem(2, is3, player -> {
            HoverIndex hover = new HoverIndex(player, ChatColor.RED, "Hoverable Text", "This is a test for some hoverable text!!");
            hover.newChat();
        });
    }
    private class SecondMenu extends ShopIndex {
        public SecondMenu() {
            super(54, "Players Menu - Page 2");
        }
    }
}
