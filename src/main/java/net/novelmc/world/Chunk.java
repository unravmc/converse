package net.novelmc.world;

import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public interface Chunk {
    interface ChunkGen {
        Biome getBiomeGenerator();

        BiomeGrid getBiomesForChunk();

        ChunkData getBlocksForChunk();

        int getChunkX();

        int getChunkZ();
    }

    void setBlocksInChunk(ChunkGen chunk);
}
