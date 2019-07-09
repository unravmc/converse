package net.novelmc.config;

import java.io.File;
import java.io.IOException;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
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
                return;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        if (f.exists())
        {
            if (!playerData.isSet("username"))
            {
                playerData.set("username", player.getName());
            }
            if (!playerData.isSet("uuid"))
            {
                playerData.set("uuid", player.getUniqueId().toString());
            }
            if (!playerData.isSet("ip"))
            {
                playerData.set("ip", player.getAddress().getHostName());
            }
            return;
        }
    }

    public FileConfiguration getPlayer(Player player)
    {
        File f = new File(userdata, File.separator + player.getName() + ".yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getPlayer(String player)
    {
        File f = new File(userdata, File.separator + player + ".yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    public void savePlayer(Player player)
    {
        File f = new File(userdata, File.separator + player.getName() + ".yml");
        try
        {
            super.save(f);
            super.load(f);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void savePlayer(String player)
    {
        File f = new File(userdata, File.separator + player + ".yml");
        try
        {
            super.save(f);
            super.load(f);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }
}