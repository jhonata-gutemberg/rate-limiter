package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import java.util.Map;
import java.util.Set;

public interface RateLimitRepository {
    Map<RateLimitCollectionKey, Set<RateLimit>> findAll();
}
