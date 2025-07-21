package dev.gutemberg.rate.limiter.domain.token.bucket.repositories;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import java.util.Optional;

public interface TokenBucketRepository {
    Optional<TokenBucket> findOneByConfigKeyAndIdentifier(String configKey, String identifier);
    void save(String configKey, TokenBucket tokenBucket);
}
