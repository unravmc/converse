package net.novelmc.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends ShopIndex {
    public MainMenu() {
        super(9, "");
        
        setItem(1, new ItemStack(Material.STICK), player -> {
            player.sendMessage("This is a test.");
        });
    }
}