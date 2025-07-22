package dev.gutemberg.rate.limiter.domain.rate.limit.contracts.schedulers;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketRefill;

public interface TokenBucketRefillScheduler {
    void schedule(TokenBucketRefill refill);
}
