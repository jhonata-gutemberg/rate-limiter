package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimitCollection;
import java.util.Set;

public interface RateLimitCollectionRepository {
    Set<RateLimitCollection> findAll();
}
