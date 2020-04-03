package net.novelmc.world.properties;

import org.bukkit.NamespacedKey;

public interface Properties {
    NamespacedKey TEMPERATURE = NamespacedKey.minecraft("temperature");

    NamespacedKey WETNESS = NamespacedKey.minecraft("wetness");

    NamespacedKey BASE_HEIGHT = NamespacedKey.minecraft("base_height");

    NamespacedKey HEIGHT_VARIATION = NamespacedKey.minecraft("height_variation");

    NamespacedKey WORLD_SEED = NamespacedKey.minecraft("world_seed");

    NamespacedKey SEA_LEVEL = NamespacedKey.minecraft("sea_level");

    FloatProperty getFloat(NamespacedKey name, float defaultValue);

    <T> Property<T> getProperty(NamespacedKey name, T defaultValue);
}
