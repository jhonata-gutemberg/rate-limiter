package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.ApplyRateLimitUseCaseInput;

public class RateLimitConfigKeyBuilder {
    private RateLimitConfigKeyBuilder() {}

    public static String build(final ApplyRateLimitUseCaseInput input) {
        return input.action().name().toLowerCase() + ":" + input.resource();
    }
}
