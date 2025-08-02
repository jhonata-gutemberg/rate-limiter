package dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig;
import java.util.Optional;
import java.util.Set;

public interface RateLimitConfigCacheRepository {
    Optional<RateLimitConfig> findOneByKey(String key);
    void save(Set<RateLimitConfig> configs);
}
