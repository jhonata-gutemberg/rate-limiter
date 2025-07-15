package dev.gutemberg.rate.limiter.infrastructure.converters;

import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;

public class RateLimitCollectionKeyToStringConverter {
    private RateLimitCollectionKeyToStringConverter() {}

    public static String convert(RateLimitCollectionKey input) {
        return input.action().name().toLowerCase() + ":" + input.resource();
    }
}
