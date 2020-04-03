package net.novelmc.world;

public interface Terrain extends Chunk {
    enum HeightType {
        WORLD_SURFACE,
        OCEAN_FLOOR
    }

    int getHeight(int x, int z, HeightType type);
}
