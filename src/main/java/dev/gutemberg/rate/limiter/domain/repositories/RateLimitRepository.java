package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import java.util.List;

public interface RateLimitRepository {
    List<RateLimit> findAllByCollectionKey(RateLimitCollectionKey rateLimitCollectionKey);
}
