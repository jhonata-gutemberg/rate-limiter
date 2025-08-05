package dev.gutemberg.rate.limiter.vendors.domain.rate.limit.contracts.schedulers;

import dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities.TokenBucketRefill;

public interface TokenBucketRefillScheduler {
    void schedule(TokenBucketRefill refill);
}
