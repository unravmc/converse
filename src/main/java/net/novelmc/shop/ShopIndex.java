package net.novelmc.shop;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopIndex implements InventoryHolder, Listener {
    private final Inventory INV;
    
    @SuppressWarnings("")
    public ShopIndex() {
        INV = Bukkit.createInventory(this, 9, "UnraveledMC Shop");
    }
    
    @Override
    public Inventory getInventory() {
        return INV;
    }
    
    //Init
    public void initializeItems() {
        INV.addItem(createGuiItem(Material.DIAMOND, "Option 1", "Lore Line 1", "Lore Line 2"));
    }
    // Create new gui item in inventory;
    @SuppressWarnings("")
    private ItemStack createGuiItem(Material mat, String name, String...lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metaLore = new ArrayList<>();
        
        metaLore.addAll(Arrays.asList(lore));
        
        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }
    
    //open the actual menu
    public void openInventory(Player p) {
        p.openInventory(INV);
    }
}
