package dev.gutemberg.rate.limiter.domain.token.bucket.models;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit.Unit;

public class TokenBucketKeyBuilder {
    private TokenBucketKeyBuilder() {}

    public static String build(final String configKey, final String limitIdentifier, final Unit rateUnit) {
        return configKey + ":" + limitIdentifier + ":" + rateUnit;
    }
}
