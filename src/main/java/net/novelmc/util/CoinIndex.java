package net.novelmc.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class CoinIndex extends ConverseBase {
    //Coins
    public Map<UUID, Integer> coinMap = new HashMap<>();
    
    public void setCoins(Player p, int x) {
        UUID unique = p.getUniqueId();
        if (!coinMap.containsKey(unique)) {
            coinMap.put(unique, x);
        } else {
            coinMap.replace(unique, x);
        }
    }
    
    public int getCoins(Player p) {
        UUID unique = p.getUniqueId();
        return coinMap.getOrDefault(unique, 0);
    }
    
    public void addCoins(Player p, int x) {
        UUID unique = p.getUniqueId();
        if (!coinMap.containsKey(unique)) {
            coinMap.put(unique, x);
        } else {
            int y = coinMap.get(unique);
            int z = x + y;
            coinMap.replace(unique, z);
        }
    }
    
    public void removeCoins(Player p, int index) {
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
}