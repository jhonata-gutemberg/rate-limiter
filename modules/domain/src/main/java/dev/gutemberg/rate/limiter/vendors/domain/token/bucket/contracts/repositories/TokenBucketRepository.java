package dev.gutemberg.rate.limiter.vendors.domain.token.bucket.contracts.repositories;

import dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities.TokenBucket;
import java.util.Optional;

public interface TokenBucketRepository {
    Optional<TokenBucket> findOneByConfigKeyAndIdentifier(String configKey, String identifier);
    void save(String configKey, TokenBucket tokenBucket);
}
