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
}
