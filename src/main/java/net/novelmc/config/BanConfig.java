package net.novelmc.config;

import java.io.File;
import java.io.IOException;
import net.novelmc.Converse;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class BanConfig extends YamlConfiguration
{
    private static BanConfig config;
    private final Converse plugin;
    private final File file;

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
        catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            super.save(file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveDefault()
    {
        plugin.saveResource("bans.yml", false);
    }
}
