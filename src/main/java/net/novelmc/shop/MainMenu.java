package net.novelmc.shop;

import net.novelmc.Converse;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu extends ShopIndex {
    public MainMenu() {
        super(9, "UnraveledMC Shop");
        ItemStack is = newItem(Material.NETHER_STAR, "Trails", "Use this to get trails with voter coins!");
        setItem(0, is, player -> {
            Converse.plugin.trails.open(player);
        });
    }
}