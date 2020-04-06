package net.novelmc.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Config extends YamlConfiguration {
    private final Plugin plugin;
    private final File configFile;
    private final boolean copyDefaults;

    public Config(Plugin plugin, String fileName, boolean versatile) {
        this(plugin, ConfigIndex.getPluginFile(plugin, fileName), versatile);
    }

    public Config(Plugin plugin, File file, boolean copyDefaults) {
        this.plugin = plugin;
        this.configFile = file;
        this.copyDefaults = copyDefaults;
    }

    public void save() {
        try {
            super.save(configFile);
        } catch (Exception ex) {
            plugin.getLogger().severe("Could not load configuration file: " + configFile.getName());
        }
    }

    public void load() {
        try {
            if (copyDefaults) {
                if (!configFile.exists()) {
                    configFile.getParentFile().mkdirs();
                    try {
                        ConfigIndex.copy(plugin.getResource(configFile.getName()), configFile);
                    } catch (IOException ex) {
                        plugin.getLogger().severe("Could not write default configuration file: " + configFile.getName());
                    }
                    plugin.getLogger().info("Installed default configuration file: " + configFile.getName());
                }
                super.addDefaults(getDefaultConfig());
            }
            if (configFile.exists()) {
                super.load(configFile);
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfig() {
        return this;
    }

    public YamlConfiguration getDefaultConfig() {
        final YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(String.valueOf(plugin.getResource(configFile.getName())));
        } catch (Throwable ex) {
            plugin.getLogger().severe("Could not load default configuration: " + configFile.getName());
            return null;
        }
        return configuration;
    }
}
