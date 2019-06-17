package net.novelmc.config;

import java.io.File;
import java.io.IOException;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class BanConfig extends YamlConfiguration
{
    private static BanConfig config;
    private static Converse plugin;
    public static final String CONFIG_FILENAME = "bans.yml";
    public static File file = new File(Bukkit.getServer().getPluginManager().getPlugin(Converse.plugin.getName()).getDataFolder(), CONFIG_FILENAME);

    public BanConfig()
    {
        Converse plugin = Converse.plugin;
        file = new File(plugin.getDataFolder(), CONFIG_FILENAME);

        if (!file.exists())
        {
            saveDefault();
        }
    }

    public void load()
    {
        try
        {
            if (file.exists())
            {
                super.load(file);
                return;
            }
            super.options.copyDefaults(true);
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