package com.lagsource.snapshot;

public final class ChunkSnapshot {
    private final String worldName;
    private final int x;
    private final int z;
    private final int entityCount;

    public ChunkSnapshot(String worldName, int x, int z, int entityCount) {
        this.worldName = worldName;
        this.x = x;
        this.z = z;
        this.entityCount = entityCount;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getEntityCount() {
        return entityCount;
    }
}
