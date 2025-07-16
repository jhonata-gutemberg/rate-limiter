package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;

public record TokenBucketKey(RateLimitCollection.Key rateLimitCollectionKey, String limitIdentifier, RateUnit rateUnit) {
    @Override
    public String toString() {
        return rateLimitCollectionKey + ":" + limitIdentifier + ":" + rateUnit;
    }
}
