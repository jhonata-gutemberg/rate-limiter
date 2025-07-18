package dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import java.util.Set;

public interface RateLimitConfigRepository {
    Set<RateLimitConfig> findAll();
}
