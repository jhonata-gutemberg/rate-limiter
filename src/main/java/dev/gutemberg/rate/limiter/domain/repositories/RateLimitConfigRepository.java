package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import java.util.Set;

public interface RateLimitConfigRepository {
    Set<RateLimitConfig> findAll();
}
