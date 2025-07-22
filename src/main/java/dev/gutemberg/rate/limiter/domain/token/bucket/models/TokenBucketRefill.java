package dev.gutemberg.rate.limiter.domain.token.bucket.models;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.Unit;

public record TokenBucketRefill(String configKey, String identifier, int refillRate, Unit unit) {
}
