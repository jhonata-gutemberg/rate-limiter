package dev.gutemberg.rate.limiter.infrastructure.redis.models;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;

public record TokenBucketValue(int availableTokens) {
    public static TokenBucketValue from(final TokenBucket tokenBucket) {
        return new TokenBucketValue(tokenBucket.availableTokens());
    }
}
