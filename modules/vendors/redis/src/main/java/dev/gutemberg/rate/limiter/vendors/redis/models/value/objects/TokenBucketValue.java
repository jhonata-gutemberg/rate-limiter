package dev.gutemberg.rate.limiter.vendors.redis.models.value.objects;

import dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities.TokenBucket;
import java.time.Instant;

public record TokenBucketValue(int availableTokens, Instant nextRefillAt) {
    public static TokenBucketValue from(final TokenBucket tokenBucket) {
        return new TokenBucketValue(tokenBucket.availableTokens(), tokenBucket.nextRefillAt());
    }
}
