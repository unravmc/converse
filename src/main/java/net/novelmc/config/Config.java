package net.novelmc.config;

import java.io.File;
import java.io.IOException;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
    public static final String CONFIG_FILENAME = "config.yml";
    public static File config = new File(Bukkit.getServer().getPluginManager().getPlugin(Converse.plugin.getName()).getDataFolder(), CONFIG_FILENAME);

    public static YamlConfiguration getConfig()
    {
        return YamlConfiguration.loadConfiguration(config);
    }

    public static void reloadConfig(FileConfiguration configuration)
    {
        try
        {
            configuration.load(config);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration save()
    {
        try
        {
            getConfig().save(config);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
