package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.models.TokenBucketKey;
import java.util.Optional;

public interface TokenBucketRepository {
    Optional<TokenBucket> findOneByKey(TokenBucketKey tokenBucketKey);
    void save(TokenBucket tokenBucket);
}
