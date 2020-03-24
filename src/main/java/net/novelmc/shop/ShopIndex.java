package net.novelmc.shop;

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public abstract class ShopIndex implements InventoryHolder, Listener {
    private final Inventory INV;
    private Map<Integer, GUIAction> actions;
    private UUID uuid;
    //
    public static Map<UUID, ShopIndex> invByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
    //
    public static Map<UUID, Integer> coinMap;
    
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
    
    public final void setItem(int slot, ItemStack stack, GUIAction action) {
        INV.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }
    
    public final void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }
    
    public final void open(Player p) {
        p.openInventory(INV);
        openInventories.put(p.getUniqueId(), getUUId());
    }
    
    public final void delete() {
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
    
    @SuppressWarnings("null")
    public final ItemStack newItem(Material mat, String name, String...lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metaLore = new ArrayList<>();
        metaLore.addAll(Arrays.asList(lore));
        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }
    
    public static final void setCoins(Player p, int x) {
        UUID unique = p.getUniqueId();
        if (!coinMap.containsKey(unique)) {
            coinMap.put(unique, x);
        } else {
            coinMap.replace(unique, x);
        }
    }
    
    public static final Integer getCoins(Player p) {
        UUID unique = p.getUniqueId();
        if (!coinMap.containsKey(unique)) {
            return 0;
        } else { 
            return coinMap.get(unique);
        }
    }
    
    public static final void addCoins(Player p, int x) {
        UUID unique = p.getUniqueId();
        if (!coinMap.containsKey(unique)) {
            setCoins(p, x);
        } else {
            int y = getCoins(p);
            int z = x + y;
            coinMap.replace(unique, z);
        }
    }
    
    public static final void removeCoins(Player p, Integer index) {
        //Completely remove entry if specified coins are higher than the amount stored.
        if (index > coinMap.get(p.getUniqueId())) {
            coinMap.remove(p.getUniqueId());
        }
        
        //do nothing if it's 0 or less.
        if (index < 1) {
            return;
        }
        
        //Takes the difference then updates the map value.
        if (index < coinMap.get(p.getUniqueId())) {
            int x = coinMap.get(p.getUniqueId());
            int newIndex = index - x;
            coinMap.replace(p.getUniqueId(), newIndex);
        }
    }
    
    public final ItemStack newPlayerHead(Player p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta sku = (SkullMeta) item.getItemMeta();
        sku.setDisplayName(p.getName());
        ArrayList<String> loreComments = new ArrayList<>();
        loreComments.add("Click for user specific info");
        sku.setLore(loreComments);
        sku.setOwningPlayer((OfflinePlayer) p);
        item.setItemMeta(sku);
        return item;
    }
    
    
}
