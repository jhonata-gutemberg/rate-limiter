package dev.gutemberg.rate.limiter.infrastructure.redis;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RedisRateLimitRepository implements RateLimitRepository {
    private final RedisTemplate<RateLimitCollectionKey, List<RateLimit>> redisTemplate;

    public RedisRateLimitRepository(final RedisTemplate<RateLimitCollectionKey, List<RateLimit>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<RateLimit> findAllByCollectionKey(final RateLimitCollectionKey rateLimitCollectionKey) {
        return redisTemplate.opsForValue().get(rateLimitCollectionKey);
    }
}
