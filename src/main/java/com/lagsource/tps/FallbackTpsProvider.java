package com.lagsource.tps;

import java.util.OptionalDouble;

public final class FallbackTpsProvider implements TpsProvider {
    @Override
    public OptionalDouble getTps() {
        return OptionalDouble.empty();
    }
}
