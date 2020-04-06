package net.novelmc.util;

import java.util.HashMap;
import java.util.Map;

import net.novelmc.Converse;
import org.bukkit.entity.Player;

public class CoinIndex {
    //Coins

    public void setCoins(Player player, int value) {
        save(player, value);
    }

    public void addCoins(Player player, int x) {
        int y = load(player);
        int z = x + y;
        save(player, z);
    }

    public void removeCoins(Player player, int index) {
        //do nothing if it's 0 or less.
        if (index < 1) return;

        if (index > load(player)) {
            save(player, 0);
        }

        //Takes the difference then updates the map value.
        if (index < load(player)) {
            int newIndex = load(player) - index;
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