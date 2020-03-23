package net.novelmc.shop;

import net.novelmc.Converse;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu extends ShopIndex {
    public MainMenu() {
        super(9, "UnraveledMC Shop");
        ItemStack is1 = newItem(Material.NETHER_STAR, "Trails", "Use this to get trails with voter coins!");
        ItemStack is2 = newItem(Material.PLAYER_HEAD, "Players", "Use this to open a list of all players.");
        setItem(0, is1, player -> {
            Converse.plugin.trails.open(player);
        });
        setItem(1, is2, player -> {
            if (!player.hasPermission("converse.staff")) {
                player.sendMessage("You cannot do that.");
                return;
            }
            Converse.plugin.players.open(player);
        });
    }
}