package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import java.util.Optional;

public interface TokenBucketRepository {
    Optional<TokenBucket> findOneByKey(String tokenBucketKey);
    void save(TokenBucket tokenBucket);
}
