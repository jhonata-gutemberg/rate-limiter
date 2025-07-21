package dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import java.util.Optional;
import java.util.Set;

public interface RateLimitConfigCacheRepository {
    Optional<RateLimitConfig> findOneByKey(String key);
    Set<RateLimitConfig> findAll();
    void save(Set<RateLimitConfig> configs);
}
