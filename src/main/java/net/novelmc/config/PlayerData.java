package net.novelmc.config;

import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerData {
    private PlayerData config;
    private File userdata;
    private YamlConfiguration playerData;

    private String name = methods.username.name().toLowerCase();
    private String uuid = methods.uuid.name().toLowerCase();
    private String ip = methods.ip.name().toLowerCase();
    private String coins = methods.coins.name().toLowerCase();

    public PlayerData(Converse plugin) {
        this.userdata = new File(plugin.getDataFolder(), File.separator + "players");
        this.playerData = new YamlConfiguration();
        this.config = this;
        if (!userdata.exists()) {
            userdata.mkdirs();
        }
    }

    public void createPlayer(Player player) {
        File f = new File(userdata, player.getName() + ".yml");

        if (!f.exists()) {
            try {
                for (methods method : methods.values()) {
                    playerData.createSection(method.name());
                }
                playerData.set(name, player.getName());
                playerData.set(uuid, player.getUniqueId().toString());
                playerData.set(ip, player.getAddress().getAddress().getHostAddress().replace("\\.", "\\_"));
                playerData.set(coins, 0);
                playerData.save(f);

                Bukkit.getLogger().info("Creating new player entry for " + player.getName());
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (f.exists()) {
            if (!playerData.isSet(name)) {
                playerData.set(name, player.getName());
            }
            if (!playerData.isSet(uuid)) {
                playerData.set(uuid, player.getUniqueId().toString());
            }
            if (!playerData.isSet(ip)) {
                playerData.set(ip, player.getAddress().getAddress().getHostAddress().replace("\\.", "\\_"));
            }
            if (!playerData.isSet(coins)) {
                playerData.set(coins, 0);
            }
        }
    }

    public YamlConfiguration getPlayer(Player player) {
        File f = new File(userdata, player.getName() + ".yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    public YamlConfiguration getPlayer(String player) {
        File f = new File(userdata, player + ".yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    public void savePlayer(Player player) {
        File f = new File(userdata, player.getName() + ".yml");
        try {
            playerData.save(f);
            playerData.load(f);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private enum methods {
        username,
        uuid,
        ip,
        coins
    }
}