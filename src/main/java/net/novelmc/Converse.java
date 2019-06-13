package net.novelmc;

import java.io.InputStream;
import java.util.Properties;
import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.permban.PermbanListener;
import net.novelmc.commands.AdminchatCommand;
import net.novelmc.commands.PermbanCommand;
import net.novelmc.commands.ConverseCommand;
import net.novelmc.commands.FlatworldCommand;
import net.novelmc.commands.GraychatCommand;
import net.novelmc.commands.ModeCommand;
import net.novelmc.commands.MuteCommand;
import net.novelmc.commands.StaffCommand;
import net.novelmc.commands.StaffworldCommand;
import net.novelmc.commands.UnpermbanCommand;
import net.novelmc.config.BanConfig;
import net.novelmc.config.Config;
import net.novelmc.listeners.ChatListener;
import net.novelmc.listeners.ModeListener;
import net.novelmc.listeners.MuteListener;
import net.novelmc.listeners.StaffListener;
import net.novelmc.listeners.WorldListener;
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
    public Config config;
    public BanConfig banConfig;

    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
        config = new Config(plugin);
        banConfig = new BanConfig(plugin);
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

    public void onDisable()
    {
        try
        {
            Updater updater = new Updater(this);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            getLogger().info("There was an error checking for an update");
        }
        config.save();
        banConfig.save();
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

    private void registerCommands()
    {
        getCommand("adminchat").setExecutor(new AdminchatCommand());
        getCommand("converse").setExecutor(new ConverseCommand());
        getCommand("flatworld").setExecutor(new FlatworldCommand());
        getCommand("graychat").setExecutor(new GraychatCommand());
        getCommand("mode").setExecutor(new ModeCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("permban").setExecutor(new PermbanCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("staffworld").setExecutor(new StaffworldCommand());
        getCommand("unpermban").setExecutor(new UnpermbanCommand());
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PermbanListener(), this);
        getServer().getPluginManager().registerEvents(new ModeListener(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new StaffListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    private void loadConfigs()
    {
        config.load();
        banConfig.load();
    }

    public static class BuildProperties
    {
        public String author;
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
