package dev.gutemberg.rate.limiter.vendors.domain.rate.limit.contracts.repositories;

import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig;
import java.util.Set;

public interface RateLimitConfigRepository {
    Set<RateLimitConfig> findAll();
}
