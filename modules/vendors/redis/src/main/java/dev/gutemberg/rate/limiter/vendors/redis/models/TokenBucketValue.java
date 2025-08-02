package dev.gutemberg.rate.limiter.vendors.redis.models;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import java.time.Instant;

public record TokenBucketValue(int availableTokens, Instant nextRefillAt) {
    public static TokenBucketValue from(final TokenBucket tokenBucket) {
        return new TokenBucketValue(tokenBucket.availableTokens(), tokenBucket.nextRefillAt());
    }
}
