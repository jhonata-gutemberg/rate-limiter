package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;

public record TokenBucketKey(String configKey, String limitIdentifier, RateLimitConfig.Limit.Unit rateUnit) {
    @Override
    public String toString() {
        return configKey + ":" + limitIdentifier + ":" + rateUnit;
    }
}
