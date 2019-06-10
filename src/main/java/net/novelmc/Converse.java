package net.novelmc;

import java.io.InputStream;
import java.util.Properties;
import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.banning.BanListener;
import net.novelmc.commands.AdminchatCommand;
import net.novelmc.commands.BanCommand;
import net.novelmc.commands.ConverseCommand;
import net.novelmc.commands.StaffCommand;
import net.novelmc.commands.UnbanCommand;
import net.novelmc.config.BanConfig;
import net.novelmc.config.Config;
import net.novelmc.listeners.StaffListener;
import net.novelmc.util.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Converse extends JavaPlugin
{
    public static Converse plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;

    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
    }

    public void onEnable()
    {
        build.load(this);
        new Metrics(this);
        getLuckPermsAPI();
        loadConfigs();
        registerCommands();
        registerListeners();
    }

    public static LuckPermsApi getLuckPermsAPI()
    {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null)
        {
            return provider.getProvider();
        }
        return null;
    }


    public void onDisable()
    {
        try
        {
            Updater updater = new Updater(this);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            plugin.getLogger().info("There was an error checking for an update");
        }
        Config.save();
        BanConfig.save();
    }

    private void registerCommands()
    {
        getCommand("adminchat").setExecutor(new AdminchatCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("converse").setExecutor(new ConverseCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
    }

    private void registerListeners()
    {
        plugin.getServer().getPluginManager().registerEvents(new BanListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new StaffListener(), this);
    }

    public void loadConfigs()
    {
        Config.getConfig();
        BanConfig.getConfig();
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
