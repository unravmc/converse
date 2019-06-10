package net.novelmc.config;

import java.io.File;
import java.io.IOException;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration
{
    private static Converse plugin;
    private static Config config;
    public static final String CONFIG_FILENAME = "config.yml";
    public static File file = new File(Bukkit.getServer().getPluginManager().getPlugin(Converse.plugin.getName()).getDataFolder(), CONFIG_FILENAME);

    public Config(Converse plugin)
    {
        Converse.plugin = plugin;
        if (!file.exists())
        {
            saveDefault();
        }
    }

    public void load()
    {
        try
        {
            super.load(file);
        }
        catch (IOException | InvalidConfigurationException ex)
        {
            ex.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            super.save(file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void saveDefault()
    {
        plugin.saveResource(CONFIG_FILENAME, false);
    }
}