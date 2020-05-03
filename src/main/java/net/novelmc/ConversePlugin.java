package net.novelmc;

import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.bans.BanManager;
import net.novelmc.bridge.LuckPermsBridge;
import net.novelmc.commands.*;
import net.novelmc.commands.loader.CommandHandler;
import net.novelmc.commands.loader.CommandLoader;
import net.novelmc.config.MainConfig;
import net.novelmc.listeners.*;
import net.novelmc.playerdata.PlayerDataListener;
import net.novelmc.playerdata.PlayerDataManager;
import net.novelmc.shop.MainMenu;
import net.novelmc.shop.PlayersMenu;
import net.novelmc.shop.TrailsMenu;
import net.novelmc.util.AprilFools;
import net.novelmc.util.PlayerOrganizer;
import net.novelmc.util.Reflect;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class ConversePlugin extends JavaPlugin {
    public static ConversePlugin plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;
    public static Util util;
    // Configs
    public MainConfig config;
    public PlayerOrganizer po;
    // Banning
    public BanManager banManager;
    // LuckPerms
    public LuckPermsBridge lp;
    // Shop
    public MainMenu shop;
    public TrailsMenu trails;
    public PlayersMenu players;
    public ShopListener shl;
    public AprilFools af;
    // Listeners
    public BanListener bl;
    public ChatListener cl;
    public ModeListener ml;
    public MuteListener mul;
    public TabListener sl;
    public WorldListener wl;
    public CageListener cgl;
    public PlaytimeListener ptl;
    public PlayerDataListener pdl;
    public ManageListener mgrl;

    // Player Data
    public PlayerDataManager playerDataManager;
    // Reflections
    public Reflect reflect;


    @Override
    public void onLoad() {
        plugin = this;
        server = plugin.getServer();
        config = new MainConfig(this);
        reflect = new Reflect(Converse.class);
    }

    @Override
    public void onEnable() {
        // Config
        registerConfigs();
        af = new AprilFools(this);
        // BuildProperties
        build.load(this);
        // LuckPerms
        getLuckPermsAPI();
        lp = new LuckPermsBridge(this);
        // Player Data Manager
        playerDataManager = new PlayerDataManager();
        // Listener
        registerListeners();
        // Banning
        banManager = new BanManager();
        // Shops
        loadShops();
        util = new Util();
        //Scoreboard for Tablist
        po = new PlayerOrganizer();
        //Commands
        new BukkitRunnable() {
            @Override
            public void run() {
                CommandLoader.getInstance().scan();
            }
        }.runTaskLater(this, 20L);
    }

    @Override
    public void onDisable() {
        // Unregister configs
        unregisterConfigs();

        // Undo cages
        for (UUID u : cgl.cages.keySet()) {
            Cage.Cager cage = cgl.cages.get(u);
            cage.undo();
        }
        cgl.cages.clear();

        // Playtime Handler
        ptl.scheduler.cancel();

        // Player Data Handler
        playerDataManager.scheduler.cancel();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        return CommandHandler.handle(sender, cmd, lbl, args);
    }

    private void loadShops() {
        shop = new MainMenu();
        trails = new TrailsMenu();
        players = new PlayersMenu();
    }

    public static LuckPermsApi getLuckPermsAPI() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager()
                .getRegistration(LuckPermsApi.class);
        if (provider != null) {
            return provider.getProvider();
        }
        return null;
    }

    private void registerListeners() {
        bl = new BanListener(this);
        cl = new ChatListener(this);
        ml = new ModeListener(this);
        mul = new MuteListener(this);
        pdl = new PlayerDataListener(this);
        sl = new TabListener(this);
        wl = new WorldListener(this);
        shl = new ShopListener(this);
        cgl = new CageListener(this);
        ptl = new PlaytimeListener(this);
        mgrl = new ManageListener(this);
    }

    public void registerConfigs() {
        config.load();
    }

    private void unregisterConfigs() {
        config.save();
    }


    public static class BuildProperties {
        public String author;
        public String version;
        public String number;
        public String date;
        public String head;

        void load(ConversePlugin plugin) {
            try {
                final Properties props;

                try (InputStream in = plugin.getResource("build.properties")) {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                version = props.getProperty("buildVersion", plugin.getDescription().getVersion());
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            } catch (IOException ex) {
                server.getLogger().severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                server.getLogger().severe(ex.toString());
            }
        }
    }
}
