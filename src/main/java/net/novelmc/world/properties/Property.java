package net.novelmc.world.properties;

import net.novelmc.world.Referencer;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Property<T> implements Keyed {
    private static <T> List<T> singletonArrayList(@Nullable T value) {
        T[] singleArray = (T[]) new Object[1];
        singleArray[0] = value;
        return Arrays.asList(singleArray);
    }

    private final Map<Referencer, List<T>> allWorldValues = new ConcurrentHashMap<>();
    private List<T> defaultValues = singletonArrayList(null);
    protected final NamespacedKey name;

    private final Object mutationLock = new Object();

    public Property(NamespacedKey name, T defaultValue) {
        this.name = Objects.requireNonNull(name);
        setDefault(defaultValue);
    }

    public final T get(Referencer world) {
        T value = getWorldDefault(world);
        if (value != null) {
            return value;
        }

        return getDefault();
    }

    public final T get(Referencer world, Biome biome) {
        T value = getBiomeInWorldDefault(world, biome);
        if (value != null) {
            return value;
        }

        value = getBiomeDefault(biome);
        if (value != null) {
            return value;
        }

        return get(world);
    }

    protected @Nullable T getBiomeDefault(Biome biome) {
        List<T> defaultValues = this.defaultValues;
        if (defaultValues.size() == 1) {
            return null;
        }
        return defaultValues.get(biome.ordinal());
    }

    protected @Nullable T getBiomeInWorldDefault(Referencer world, Biome biome) {
        List<T> worldValues = allWorldValues.get(world);
        if (worldValues == null || worldValues.size() == 1) {
            return null;
        }
        return worldValues.get(biome.ordinal());
    }

    public T getDefault() {
        List<T> defaultValues = this.defaultValues;
        return defaultValues.get(defaultValues.size() - 1);
    }

    @Override
    public NamespacedKey getKey() {
        return this.name;
    }

    protected @Nullable T getWorldDefault(Referencer world) {
        List<T> worldValues = allWorldValues.get(world);
        if (worldValues != null) {
            return worldValues.get(worldValues.size() - 1);
        }
        return null;
    }

    public void setBiomeDefault(Biome biome, T value) {
        Objects.requireNonNull(biome, "biome");
        Objects.requireNonNull(value, "value");

        synchronized (mutationLock) {
            if (defaultValues.size() == 1) {
                T globalDefault = defaultValues.get(0);
                defaultValues = (List<T>) Arrays.asList(new Object[Biome.values().length + 1]);

                defaultValues.set(defaultValues.size() - 1, globalDefault);
            }

            defaultValues.set(biome.ordinal(), value);
        }
    }

    public void setBiomeInWorldDefault(Referencer world, Biome biome, T value) {
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(biome, "biome");
        Objects.requireNonNull(value, "value");

        synchronized (mutationLock) {
            List<T> worldValues = this.allWorldValues.get(world);
            if (worldValues == null) {
                worldValues = (List<T>) Arrays.asList(new Object[Biome.values().length + 1]);
                this.allWorldValues.put(world, worldValues);
            } else if (worldValues.size() == 1) {
                T worldDefault = worldValues.get(0);
                worldValues = (List<T>) Arrays.asList(new Object[Biome.values().length + 1]);
                worldValues.set(worldValues.size() - 1, worldDefault);
                this.allWorldValues.put(world, worldValues);
            }
            worldValues.set(biome.ordinal(), value);
        }
    }

    public void setDefault(T value) {
        Objects.requireNonNull(value, "value");
        synchronized (mutationLock) {
            defaultValues.set(defaultValues.size() - 1, value);
        }
    }

    public void setWorldDefault(Referencer world, T value) {
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(value, "value");

        synchronized (mutationLock) {
            List<T> worldValues =this.allWorldValues.get(world);
            if (worldValues == null) {
                worldValues = singletonArrayList(value);
                this.allWorldValues.put(world, worldValues);
                return;
            }

            worldValues.set(worldValues.size() - 1, value);
        }
    }

    @Override
    public String toString() {
        return "Property[" + this.name.toString() + "]";
    }
}