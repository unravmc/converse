package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import net.novelmc.ConversePlugin;
import net.novelmc.util.HoverIndex;
import net.md_5.bungee.api.ChatColor;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends ShopIndex {
    public MainMenu() {
        super(9, "UnraveledMC Shop");
        ItemStack is1 = newItem(Material.NETHER_STAR, "Trails", "Use this to get trails with voter coins!");
        ItemStack is2 = newItem(Material.PLAYER_HEAD, "Players", "Use this to open a list of all players.");
        ItemStack is3 = newItem(Material.FEATHER, "Hover Chat", "Hover chat test. Click this to close the gui and print a hoverable text block!");

        setItem(0, is1, player -> ConversePlugin.plugin.trails.open(player));

        setItem(1, is2, player -> {
            registerPlayers();
            ConversePlugin.plugin.players.open(player);
        });
        //
        setItem(2, is3, player -> {
            HoverIndex hover = new HoverIndex(player, ChatColor.RED, "Hoverable Text", "This is a test for some hoverable text!!");
            hover.newChat();
            ConversePlugin.plugin.shop.open(player);
        });
    }

    private void registerPlayers() {
        for (Player target : Bukkit.getOnlinePlayers()) {
            final int[] x = {0};
            if (Util.size() > 54) {
                while (x[0] <= 52) {
                    ItemStack is = newPlayerHead(target);
                    ConversePlugin.plugin.players
                            .setItem(x[0], is, pl -> pl.sendMessage("You clicked on " + target.getName()));
                    x[0]++;
                }
                final int[] y = {0};
                SecondMenu second = new SecondMenu();
                ItemStack is4 = newItem(Material.RED_WOOL, "Previous", "Return to the previous page.");
                ItemStack is5 = newItem(Material.GREEN_WOOL, "Next", "Go to the next page.");
                setItem(53, is5, pl -> {
                    while (x[0] < Util.size()) {
                        ItemStack is = newPlayerHead(target);
                        second.setItem(y[0], is, p -> p.sendMessage("You clicked on " + target.getName()));
                        y[0]++;
                        x[0]++;
                    }
                    second.setItem(Util.size() + 2, is4, p -> {
                        ConversePlugin.plugin.players.open(p);
                    });
                    second.open(pl);
                });
            } else {
                ItemStack is = newPlayerHead(target);
                ConversePlugin.plugin.players
                        .setItem(x[0], is, pl -> pl.sendMessage("You clicked on " + target.getName()));
                x[0]++;
            }
        }
    }

    private class SecondMenu extends ShopIndex {
        public SecondMenu() {
            super(54, "Players Menu - Page 2");
        }
    }
}
