package net.novelmc.shop;

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class ShopIndex implements InventoryHolder, Listener {
    private final Inventory INV;
    private Map<Integer, GUIAction> actions;
    private UUID uuid;
    //
    public static Map<UUID, ShopIndex> invByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
    
    @SuppressWarnings("")
    public ShopIndex(int invSize, String invName) {
        uuid = UUID.randomUUID();
        INV = Bukkit.createInventory(null, invSize, invName);
        actions = new HashMap<>();
        invByUUID.put(getUUId(), this);
    }
    
    public UUID getUUId() {
        return uuid;
    }
    
    @Override
    public Inventory getInventory() {
        return INV;
    }
    
    public interface GUIAction {
        void click(Player player);
    }
    
    public void setItem(int slot, ItemStack stack, GUIAction action) {
        INV.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }
    
    public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }
    
    public void open(Player p) {
        p.openInventory(INV);
        openInventories.put(p.getUniqueId(), getUUId());
    }
    
    public void delete() {
        Bukkit.getOnlinePlayers().forEach((p) ->
        {
            UUID u = openInventories.get(p.getUniqueId());
            if (u.equals(getUUId()))
            {
                p.closeInventory();
            }
        });
        invByUUID.remove(getUUId());
    }
    
    public static Map<UUID, ShopIndex> getInvByUUID() {
        return invByUUID;
    }
    
    public static Map<UUID, UUID> getOpenInvs() {
        return openInventories;
    }
    
    public Map<Integer, GUIAction> getActions() {
        return actions;
    }
}
