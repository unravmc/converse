package net.novelmc.config;

import java.io.File;
import java.io.IOException;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData extends YamlConfiguration
{
    private static PlayerData config;
    private Converse plugin;
    private File userdata;

    public PlayerData(Converse plugin)
    {
        this.plugin = plugin;
        this.userdata = new File(plugin.getDataFolder(), File.separator + "players");

        if (!userdata.exists())
        {
            userdata.mkdir();
        }
    }

    public void createPlayer(Player player)
    {
        File f = new File(userdata, File.separator + player.getName() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        if (!f.exists())
        {
            try
            {
                playerData.set("username", player.getName());
                playerData.set("uuid", player.getUniqueId().toString());
                playerData.set("ip", player.getAddress().getHostName());
                playerData.save(f);
                Bukkit.getLogger().info("Creating new player entry for " + player.getName());
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public YamlConfiguration get(Player player)
    {
        File f = new File(userdata, File.separator + player + ".yml");
        return YamlConfiguration.loadConfiguration(f);
    }
}