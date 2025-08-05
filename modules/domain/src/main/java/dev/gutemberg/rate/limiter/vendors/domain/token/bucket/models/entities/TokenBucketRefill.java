package dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities;

import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig.Limit.Unit;

public record TokenBucketRefill(String configKey, String identifier, int refillRate, Unit unit) {
}
