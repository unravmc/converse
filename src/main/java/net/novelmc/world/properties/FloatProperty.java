package net.novelmc.world.properties;

import net.novelmc.world.Referencer;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FloatProperty implements Keyed {
    private final Map<Referencer, float[]> allWorldValues = new ConcurrentHashMap<>(1);
    private volatile float[] defaultValues = {0};
    protected final NamespacedKey name;

    private final Object mutationLock = new Object();

    public FloatProperty(NamespacedKey name, float defaultValue) {
        this.name = Objects.requireNonNull(name);
        setDefault(defaultValue);
    }

    private void checkForNaN(float value) {
        if (Float.isNaN(value)) throw new UnsupportedOperationException();
    }

    public final float get(Referencer world) {
        float value = getWorldDefault(world);
        if (!Float.isNaN(value)) {
            return value;
        }
        return getDefault();
    }

    public final float get(Referencer world, Biome biome) {
        float value = getBiomeInWorldDefault(world, biome);
        if (!Float.isNaN(value)) {
            return value;
        }

        value = getBiomeDefault(biome);
        if (!Float.isNaN(value)) {
            return value;
        }

        return get(world);
    }

    protected float getBiomeDefault(Biome biome) {
        float[] defaultValues = this.defaultValues;
        if (defaultValues.length == 1) {
            return Float.NaN;
        }
        return defaultValues[biome.ordinal()];
    }

    protected float getBiomeInWorldDefault(Referencer world, Biome biome) {
        float[] worldValues = allWorldValues.get(world);
        if (worldValues == null || worldValues.length == 1) {
            return Float.NaN;
        }
        return worldValues[biome.ordinal()];
    }

    protected float getDefault() {
        float[] defaultValues = this.defaultValues;
        return defaultValues[defaultValues.length - 1];
    }

    @Override
    public NamespacedKey getKey() {
        return this.name;
    }

    protected float getWorldDefault(Referencer world) {
        float[] worldValues = allWorldValues.get(world);
        if (worldValues != null) {
            return worldValues[worldValues.length - 1];
        }
        return Float.NaN;
    }

    public void setBiomeDefault(Biome biome, float value) {
        if (defaultValues.length == 1) {
            float globalDefault = defaultValues[0];
            defaultValues = new float[Biome.values().length + 1];
            Arrays.fill(defaultValues, Float.NaN);
            defaultValues[defaultValues.length - 1] = globalDefault;
        }
        defaultValues[biome.ordinal()] = value;
    }

    public void setBiomeInWorldDefault(Referencer world, Biome biome, float value) {
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(biome, "biome");
        checkForNaN(value);

        synchronized (mutationLock) {
            float[] worldValues = this.allWorldValues.get(world);
            if (worldValues == null) {
                worldValues = new float[Biome.values().length + 1];
                Arrays.fill(worldValues, Float.NaN);
                this.allWorldValues.put(world, worldValues);
            } else if (worldValues.length == 1) {
                float worldDefault = worldValues[0];
                worldValues = new float[Biome.values().length + 1];
                Arrays.fill(worldValues, Float.NaN);
                worldValues[worldValues.length - 1] = worldDefault;
                this.allWorldValues.put(world, worldValues);
            }
            worldValues[biome.ordinal()] = value;
        }
    }

    public void setDefault(float value) {
        checkForNaN(value);
        synchronized (mutationLock) {
            defaultValues[defaultValues.length - 1] = value;
        }
    }

    public void setWorldDefault(Referencer world, float value) {
        Objects.requireNonNull(world, "world");
        checkForNaN(value);

        synchronized (mutationLock) {
            float[] worldValues = this.allWorldValues.get(world);
            if (worldValues == null) {
                worldValues = new float[1];
                this.allWorldValues.put(world, worldValues);
            }

            worldValues[worldValues.length - 1] = value;
        }
    }

    @Override
    public String toString() {
        return "FloatProperty[" + this.name.toString() + "]";
    }

}