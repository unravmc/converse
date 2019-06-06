package net.novelmc;

import java.io.InputStream;
import java.util.Properties;
import net.novelmc.util.Config;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Converse extends JavaPlugin
{
    public static Converse plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;
    public static YamlConfiguration config;
    public static YamlConfiguration bans = Config.getConfig(Config.ConfigFile.BANS);

    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
    }

    public void onEnable()
    {
        build.load(this);
        new Metrics(this);
        Config.loadConfigs();
    }

    public void onDisable()
    {
    }

    public static class BuildProperties
    {
        public String author;
        public String codename;
        public String version;
        public String number;
        public String date;
        public String head;

        void load(Converse plugin)
        {
            try
            {
                final Properties props;

                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                codename = props.getProperty("buildCodename", "unknown");
                version = props.getProperty("buildVersion", plugin.getDescription().getVersion());
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            }
            catch (Exception ex)
            {
                server.getLogger().severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                server.getLogger().severe(ex.toString());
            }
        }
    }
}
