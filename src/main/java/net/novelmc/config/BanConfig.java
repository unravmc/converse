package net.novelmc.config;

import net.novelmc.Converse;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class BanConfig extends YamlConfiguration
{
    private static BanConfig config;
    private Converse plugin;
    private File file;

    public BanConfig(Converse plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "bans.yml");

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
        catch (Exception ex)
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
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveDefault()
    {
        plugin.saveResource("bans.yml", false);
    }
}
