package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TrailsMenu extends ShopIndex {
    public TrailsMenu() {
        super(9, "UnraveledMC Trails");
        ItemStack opt1 = newItem(Material.BLACK_DYE, "Black Particle Effect Trail", "Give yourself a black particle effect trail");
        setItem(0, opt1, player -> {
            player.sendMessage("This has yet to be implemented.");
        });
    }
}