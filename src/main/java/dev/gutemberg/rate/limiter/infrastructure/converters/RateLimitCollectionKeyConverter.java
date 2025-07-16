package dev.gutemberg.rate.limiter.infrastructure.converters;

import dev.gutemberg.rate.limiter.domain.models.RateLimitCollection;

public class RateLimitCollectionKeyConverter {
    private RateLimitCollectionKeyConverter() {}

    public static String toString(RateLimitCollection.Key key) {
        return key.action().name().toLowerCase() + ":" + key.resource();
    }
}
