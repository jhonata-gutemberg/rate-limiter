package dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities;

import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig.Limit.Unit;

public record TokenBucketConfig(String key) {
    public static class KeyBuilder {
        private KeyBuilder() {}

        public static String build(final String rateLimitConfigKey, final Unit unit) {
            return rateLimitConfigKey + ":" + unit.name().toLowerCase();
        }
    }
}
