package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimitCollection;
import java.util.Optional;
import java.util.Set;

public interface RateLimitCollectionCacheRepository {
    Optional<RateLimitCollection> findOneByKey(RateLimitCollection.Key key);
    void save(Set<RateLimitCollection> collections);
}
