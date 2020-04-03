package net.novelmc.world;

public interface Biome {
    default Biome[] getBiomes(int minX, int minZ, int xSize, int zSize) {
        int outX = xSize / 4;
        int outZ = zSize / 4;
        Biome[] out = getOutBiomes(minX / 4, minZ / 4, outX, outZ);
        Biome[] normal = new Biome[xSize * zSize];
        for (int i = 0; i < normal.length; i++) {
            int x = i % xSize;
            int z = i / xSize;
            normal[i] = out[(z / 4) * outZ + x / 4];
        }
        return normal;
    }

    default Biome getOutBiome(int x, int z) {
        return getOutBiomes(x, z, 1, 1)[0];
    }

    Biome[] getOutBiomes(int minX, int minZ, int xSize, int zSize);
}
