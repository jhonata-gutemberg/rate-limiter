package dev.gutemberg.rate.limiter.infrastructure.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimitCollection;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCollectionCacheRepository;
import dev.gutemberg.rate.limiter.infrastructure.converters.RateLimitCollectionKeyConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Repository
public class RedisRateLimitCollectionCacheRepository implements RateLimitCollectionCacheRepository {
    private final RedisTemplate<String, RateLimitCollection.Value> redisTemplate;

    public RedisRateLimitCollectionCacheRepository(final RedisTemplate<String, RateLimitCollection.Value> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<RateLimitCollection> findOneByKey(final RateLimitCollection.Key key) {
        return Optional.ofNullable(redisTemplate.opsForSet().members(RateLimitCollectionKeyConverter.toString(key)))
                .flatMap(buildCollection(key));
    }

    @Override
    public void save(final Set<RateLimitCollection> collections) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            collections.forEach(collection -> executor.submit(() -> save(collection)));
        }
    }

    private Function<Set<RateLimitCollection.Value>, Optional<RateLimitCollection>> buildCollection(
            final RateLimitCollection.Key key
    ) {
        return values -> values.isEmpty() ? Optional.empty()
                : Optional.of(new RateLimitCollection(key, values));
    }

    private void save(final RateLimitCollection collection) {
        final var collectionKey = RateLimitCollectionKeyConverter.toString(collection.key());
        redisTemplate.delete(collectionKey);
        collection.values().forEach(rateLimit -> redisTemplate.opsForSet().add(collectionKey, rateLimit));
    }
}
