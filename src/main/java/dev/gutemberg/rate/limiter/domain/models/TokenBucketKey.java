package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;

public record TokenBucketKey(RateLimitCollectionKey rateCollectionLimitKey, String limitIdentifier, RateUnit rateUnit) {
    @Override
    public String toString() {
        return rateCollectionLimitKey + ":" + limitIdentifier + ":" + rateUnit;
    }
}
