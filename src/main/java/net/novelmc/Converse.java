package net.novelmc;

import java.io.InputStream;
import java.util.Properties;
import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.commands.AdminchatCommand;
import net.novelmc.commands.BanCommand;
import net.novelmc.commands.ConverseCommand;
import net.novelmc.commands.DbanCommand;
import net.novelmc.commands.FlatworldCommand;
import net.novelmc.commands.GraychatCommand;
import net.novelmc.commands.ModeCommand;
import net.novelmc.commands.MuteCommand;
import net.novelmc.commands.PermbanCommand;
import net.novelmc.commands.PlotworldCommand;
import net.novelmc.commands.StaffCommand;
import net.novelmc.commands.StaffworldCommand;
import net.novelmc.commands.UnbanCommand;
import net.novelmc.config.BanConfig;
import net.novelmc.config.MainConfig;
import net.novelmc.config.PermbanConfig;
import net.novelmc.listeners.BanListener;
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
    public BanConfig banConfig;
    public MainConfig config;
    public PermbanConfig permbanConfig;

    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
        banConfig = new BanConfig(plugin);
        config = new MainConfig(plugin);
        permbanConfig = new PermbanConfig(plugin);
    }

    public void onEnable()
    {
        // Config
        registerConfigs();
        // BuildProperties
        build.load(this);
        // Metrics
        new Metrics(this);
        // LuckPerms
        getLuckPermsAPI();
        // Commands
        registerCommands();
        // Listener
        registerListeners();
    }

    public void onDisable()
    {
        // Unregister configs
        unregisterConfigs();

        // Updater
        try
        {
            Updater updater = new Updater(this);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            getLogger().info("There was an error checking for an update");
        }
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
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("converse").setExecutor(new ConverseCommand());
        getCommand("dban").setExecutor(new DbanCommand());
        getCommand("flatworld").setExecutor(new FlatworldCommand());
        getCommand("graychat").setExecutor(new GraychatCommand());
        getCommand("mode").setExecutor(new ModeCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("permban").setExecutor(new PermbanCommand());
        getCommand("plotworld").setExecutor(new PlotworldCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("staffworld").setExecutor(new StaffworldCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new BanListener(), this);
        getServer().getPluginManager().registerEvents(new ModeListener(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new StaffListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    private void registerConfigs()
    {
        banConfig.load();
        config.load();
        permbanConfig.load();
    }

    private void unregisterConfigs()
    {
        banConfig.save();
        config.save();
        permbanConfig.save();
    }

    public void reregisterConfigs()
    {
        banConfig.save();
        config.save();
        permbanConfig.save();
        banConfig.load();
        config.load();
        permbanConfig.load();
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
