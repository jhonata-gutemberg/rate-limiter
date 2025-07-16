package dev.gutemberg.rate.limiter.infrastructure.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollection;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCacheRepository;
import dev.gutemberg.rate.limiter.infrastructure.converters.RateLimitCollectionKeyConverter;
import dev.gutemberg.rate.limiter.infrastructure.converters.RateLimitCollectionKeyToStringConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Set;
import java.util.concurrent.Executors;

@Repository
public class RedisRateLimitCacheRepository implements RateLimitCacheRepository {
    private final RedisTemplate<String, RateLimitCollection.Value> redisTemplate;

    public RedisRateLimitCacheRepository(final RedisTemplate<String, RateLimit> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<RateLimit> findAllByCollectionKey(final RateLimitCollectionKey collectionKey) {
        return redisTemplate.opsForSet().members(RateLimitCollectionKeyConverter.toString(collectionKey));
    }

    @Override
    public void save(final Set<RateLimitCollection> collections) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            collections.forEach(collection -> executor.submit(() -> save(collection)));
        }
    }

    private void save(final RateLimitCollection collection) {
        final var collectionKey = RateLimitCollectionKeyConverter.toString(collection.key());
        redisTemplate.delete(collectionKey);
        collection.values().forEach(rateLimit -> redisTemplate.opsForSet().add(collectionKey, rateLimit));
    }
}
