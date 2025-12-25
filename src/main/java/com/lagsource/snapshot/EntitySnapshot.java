package com.lagsource.snapshot;

import org.bukkit.entity.EntityType;

import java.util.Map;

public final class EntitySnapshot {
    private final int total;
    private final Map<EntityType, Integer> counts;

    public EntitySnapshot(int total, Map<EntityType, Integer> counts) {
        this.total = total;
        this.counts = counts;
    }

    public int getTotal() {
        return total;
    }

    public Map<EntityType, Integer> getCounts() {
        return counts;
    }
}
