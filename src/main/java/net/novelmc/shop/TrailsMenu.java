package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import net.novelmc.util.Util;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TrailsMenu extends ShopIndex {
    public TrailsMenu() {
        super(9, "UnraveledMC Trails");
        ItemStack opt1 = newItem(Material.DRAGON_BREATH, "Dragons Breath Effect", "Give yourself dragons breath particles");
        ItemStack opt2 = newItem(Material.TORCH, "Torch Effect", "Give yourself a fiery effect trail.");
        ItemStack opt3 = newItem(Material.BAMBOO, "", "");
        setItem(0, opt1, player -> {
            putPlayerParticle(player, Particle.DRAGON_BREATH);
        });
        setItem(1, opt2, player -> {
            putPlayerParticle(player, Particle.FLAME);
        });
        setItem(2, opt3, player -> {
            if (!player.getName().equalsIgnoreCase("Dr_Coomer")) {
                putPlayerParticle(player, Particle.FALLING_DUST);
            } else {
                putPlayerParticle(player, Particle.TOWN_AURA);
            }
        });


    }

    private void putPlayerParticle(Player player, Particle particle) {
        if (!Util.map.containsKey(player)) {
            Util.map.put(player, particle);
        } else {
            Util.map.remove(player);
        }
    }
}