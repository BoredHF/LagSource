package com.lagsource.tps;

import org.bukkit.Bukkit;

import java.util.OptionalDouble;

public final class PaperTpsProvider implements TpsProvider {
    @Override
    public OptionalDouble getTps() {
        double[] tps = Bukkit.getTPS();
        if (tps == null || tps.length == 0) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(tps[0]);
    }
}
