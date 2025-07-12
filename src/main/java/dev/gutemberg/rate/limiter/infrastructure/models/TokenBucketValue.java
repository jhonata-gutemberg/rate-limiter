package dev.gutemberg.rate.limiter.infrastructure.models;

import dev.gutemberg.rate.limiter.domain.models.TokenBucket;

public record TokenBucketValue(int availableTokens) {
    public static TokenBucketValue from(final TokenBucket tokenBucket) {
        return new TokenBucketValue(tokenBucket.availableTokens());
    }
}
