package net.novelmc.config;

import java.io.File;

import net.novelmc.Converse;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig extends YamlConfiguration {
    private static MainConfig config;
    private Converse plugin;
    private File file;

    public MainConfig(Converse plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            saveDefault();
        }
    }

    public void load() {
        try {
            super.load(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveDefault() {
        plugin.saveResource("config.yml", false);
    }
}
