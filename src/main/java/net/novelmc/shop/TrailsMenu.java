package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import net.novelmc.util.Util;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class TrailsMenu extends ShopIndex {
    public TrailsMenu() {
        super(9, "UnraveledMC Trails");
        ItemStack opt1 = newItem(Material.DRAGON_BREATH, "Black Particle Effect Trail", "Give yourself a black particle effect trail");
        setItem(0, opt1, player -> {
            if (!Util.map.containsKey(player)) {
                Util.map.put(player, Particle.DRAGON_BREATH);
            } else {
                Util.map.remove(player);
            }
        });
    }
}