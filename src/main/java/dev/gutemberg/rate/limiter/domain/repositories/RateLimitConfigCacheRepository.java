package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import java.util.Optional;
import java.util.Set;

public interface RateLimitConfigCacheRepository {
    Optional<RateLimitConfig> findOneByKey(String key);
    void save(Set<RateLimitConfig> configs);
}
