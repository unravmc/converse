package net.novelmc.config;

import java.io.File;
import net.novelmc.Converse;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermbanConfig extends YamlConfiguration
{
    private static PermbanConfig config;
    private Converse plugin;
    private File file;

    public PermbanConfig(Converse plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "permbans.yml");

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
        plugin.saveResource("permbans.yml", false);
    }
}
