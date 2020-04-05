package net.novelmc.world.decor;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

public interface DecorArea {
    int DECORATION_RADIUS = 8;

    Biome getBiome(int x, int z);

    Material getBlock(int x, int y, int z);

    BlockData getBlockData(int x, int y, int z);

    BlockState getBlockState(int x, int y, int z);

    int getCenterX();

    int getCenterZ();

    void setBlock(int x, int y, int z, Material m);

    void setBlockData(int x, int y, int z, BlockState state);
}
