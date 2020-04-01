package net.novelmc.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import net.novelmc.Converse;
import net.novelmc.config.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoinIndex extends ConverseBase {
    //Coins
    private Map<UUID, Integer> coinMap = new HashMap<>();
    private PlayerData pd = Converse.plugin.pd;

    public CoinIndex(){}

    public Map<UUID, Integer> CoinIndex(UUID uuid) {
        if (!coinMap.containsKey(uuid)) {
            coinMap.put(uuid, load(uuid));
        } else {
            coinMap.replace(uuid, load(uuid));
        }
        return coinMap;
    }

    public void setCoins(UUID uuid, int x) {
        load(uuid);
        if (!coinMap.containsKey(uuid)) {
            coinMap.put(uuid, x);
        } else {
            coinMap.replace(uuid, x);
        }
        save(uuid, coinMap);
    }

    public void addCoins(UUID uuid, int x) {
        if (!coinMap.containsKey(uuid)) {
            coinMap.put(uuid, load(uuid));
        } else {
            int y = load(uuid);
            int z = x + y;
            coinMap.replace(uuid, z);
            save(uuid, coinMap);
        }
    }

    public void removeCoins(UUID uuid, int index) {
        if (!coinMap.containsKey(uuid)) return;

        //do nothing if it's 0 or less.
        if (index < 1) return;

        if (index > load(uuid)) {
            coinMap.replace(uuid, 0);
            save(uuid, coinMap);
        }

        //Takes the difference then updates the map value.
        if (index < load(uuid)) {
            int x = load(uuid);
            int newIndex = index - x;
            coinMap.replace(uuid, newIndex);
            save(uuid, coinMap);
        }
    }

    public void save(UUID uuid, @NotNull Map<UUID, Integer> map) {
        Player player = getOffline(uuid);
        Converse.plugin.pd.getPlayer(player).set("coins", map.get(player.getUniqueId()));
        Converse.plugin.pd.savePlayer(player);
    }

    public Integer load(UUID uuid) {
        Player player = getOffline(uuid);
        return (Integer) Objects.requireNonNull(Converse.plugin.pd.getPlayer(player)
                .get("coins"));
    }

    private Player getOffline(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        Player fPlayer = player.getPlayer();
        return fPlayer;
    }
}