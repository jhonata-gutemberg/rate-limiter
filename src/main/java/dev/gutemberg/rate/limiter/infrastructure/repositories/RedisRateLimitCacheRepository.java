package dev.gutemberg.rate.limiter.infrastructure.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import dev.gutemberg.rate.limiter.infrastructure.contracts.StringKeyConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public class RedisRateLimitRepository implements RateLimitRepository {
    private final StringKeyConverter<RateLimitCollectionKey> stringKeyConverter;
    private final RedisTemplate<String, RateLimit> redisTemplate;

    public RedisRateLimitRepository(
            final StringKeyConverter<RateLimitCollectionKey> stringKeyConverter,
            final RedisTemplate<String, RateLimit> redisTemplate
    ) {
        this.stringKeyConverter = stringKeyConverter;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<RateLimit> findAllByCollectionKey(final RateLimitCollectionKey collectionKey) {
        return redisTemplate.opsForSet().members(stringKeyConverter.convert(collectionKey));
    }
}
