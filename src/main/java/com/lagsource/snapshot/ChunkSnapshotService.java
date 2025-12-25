package com.lagsource.snapshot;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public final class ChunkSnapshotService {
    public List<ChunkSnapshot> snapshot(List<World> worlds) {
        List<ChunkSnapshot> snapshots = new ArrayList<>();
        if (worlds == null) {
            return snapshots;
        }

        for (World world : worlds) {
            if (world == null) {
                continue;
            }
            for (Chunk chunk : world.getLoadedChunks()) {
                int count = chunk.getEntities().length;
                if (count == 0) {
                    continue;
                }
                snapshots.add(new ChunkSnapshot(world.getName(), chunk.getX(), chunk.getZ(), count));
            }
        }

        return snapshots;
    }

    public List<ChunkSnapshot> snapshotNearby(World world, int centerX, int centerZ, int radius) {
        List<ChunkSnapshot> snapshots = new ArrayList<>();
        if (world == null || radius < 0) {
            return snapshots;
        }

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                if (!world.isChunkLoaded(x, z)) {
                    continue;
                }
                Chunk chunk = world.getChunkAt(x, z);
                int count = chunk.getEntities().length;
                if (count == 0) {
                    continue;
                }
                snapshots.add(new ChunkSnapshot(world.getName(), x, z, count));
            }
        }

        return snapshots;
    }
}
