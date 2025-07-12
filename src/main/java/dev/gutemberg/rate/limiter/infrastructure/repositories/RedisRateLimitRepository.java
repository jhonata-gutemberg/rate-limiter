package dev.gutemberg.rate.limiter.infrastructure.repositories;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public class RedisRateLimitRepository implements RateLimitRepository {
    private final RedisTemplate<String, RateLimit> redisTemplate;

    public RedisRateLimitRepository(final RedisTemplate<String, RateLimit> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<RateLimit> findAllByCollectionKey(final RateLimitCollectionKey collectionKey) {
        return redisTemplate.opsForSet().members(collectionKey.toString());
    }
}
