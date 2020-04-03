package net.novelmc.world.decor;

import com.google.common.collect.ImmutableList;

import java.util.List;

public enum DecorType {
    RAW_GEN,
    LOCAL_MODS,
    UNDERGROUND_STRUCTURES,
    SURFACE_STRUCTURES,
    ORES,
    UNDERGROUND,
    VEGETATION,
    TOP;

    private static final List<DecorType> all = ImmutableList.copyOf(values());

    public static List<DecorType> all() {
        return all;
    }
}
