package com.lagsource.snapshot;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class EntitySnapshotService {
    public EntitySnapshot snapshot(List<World> worlds, boolean includePlayers) {
        Map<EntityType, Integer> counts = new EnumMap<>(EntityType.class);
        int total = 0;

        if (worlds == null) {
            return new EntitySnapshot(0, counts);
        }

        for (World world : worlds) {
            if (world == null) {
                continue;
            }
            for (Entity entity : world.getEntities()) {
                if (!includePlayers && entity instanceof Player) {
                    continue;
                }
                EntityType type = entity.getType();
                counts.merge(type, 1, Integer::sum);
                total++;
            }
        }

        return new EntitySnapshot(total, counts);
    }
}
