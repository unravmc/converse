package net.novelmc.util;

import java.util.HashMap;
import java.util.Map;

import net.novelmc.Converse;
import org.bukkit.entity.Player;

public class CoinIndex {
    //Coins
    private Map<Player, Integer> coinMap;

    public CoinIndex() {
        coinMap = new HashMap<>();
    }

    private Map<Player, Integer> coinIndex(Player player) {
        if (!coinMap.containsKey(player)) {
            coinMap.put(player, load(player));
        }
        return coinMap;
    }

    public void setCoins(Player player, int value) {
        coinIndex(player).replace(player, value);
        save(player, coinIndex(player).get(player));
    }

    public void addCoins(Player player, int x) {
        int y = load(player);
        int z = x + y;
        coinIndex(player).replace(player, z);
        save(player, coinIndex(player).get(player));
    }

    public void removeCoins(Player player, int index) {
        if (!coinMap.containsKey(player)) return;

        //do nothing if it's 0 or less.
        if (index < 1) return;

        if (index > load(player)) {
            coinIndex(player).replace(player, 0);
            save(player, coinIndex(player).get(player));
        }

        //Takes the difference then updates the map value.
        if (index < load(player)) {
            int newIndex = load(player) - index;
            coinIndex(player).replace(player, newIndex);
            save(player, newIndex);
        }
    }

    public void save(Player player, int value) {
        Converse.plugin.pd.getPlayer(player).set("coins", value);
        Converse.plugin.pd.savePlayer(player);
    }

    public int load(Player player) {
        return Converse.plugin.pd.getPlayer(player).getInt("coins");
    }
}