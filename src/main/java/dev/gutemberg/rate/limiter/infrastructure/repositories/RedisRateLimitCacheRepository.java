package dev.gutemberg.rate.limiter.infrastructure.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCacheRepository;
import dev.gutemberg.rate.limiter.infrastructure.converters.RateLimitCollectionKeyToStringConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

@Repository
public class RedisRateLimitCacheRepository implements RateLimitCacheRepository {
    private final RedisTemplate<String, RateLimit> redisTemplate;

    public RedisRateLimitCacheRepository(final RedisTemplate<String, RateLimit> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<RateLimit> findAllByCollectionKey(final RateLimitCollectionKey collectionKey) {
        return redisTemplate.opsForSet().members(RateLimitCollectionKeyToStringConverter.convert(collectionKey));
    }

    @Override
    public void save(final Map<RateLimitCollectionKey, Set<RateLimit>> rateLimitCollections) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            rateLimitCollections.entrySet()
                    .forEach(entry -> executor.submit(() -> save(entry)));
        }
    }

    private void save(final Map.Entry<RateLimitCollectionKey, Set<RateLimit>> rateLimitEntry) {
        final var collectionKey = RateLimitCollectionKeyToStringConverter.convert(rateLimitEntry.getKey());
        redisTemplate.delete(collectionKey);
        rateLimitEntry.getValue()
                .forEach(rateLimit -> redisTemplate.opsForSet().add(collectionKey, rateLimit));
    }
}
