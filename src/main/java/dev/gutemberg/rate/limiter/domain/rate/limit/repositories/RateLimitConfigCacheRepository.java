package dev.gutemberg.rate.limiter.domain.rate.limit.repositories;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import java.util.Optional;
import java.util.Set;

public interface RateLimitConfigCacheRepository {
    Optional<RateLimitConfig> findOneByKey(String key);
    void save(Set<RateLimitConfig> configs);
}
