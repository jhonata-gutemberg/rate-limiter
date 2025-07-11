package dev.gutemberg.rate.limiter.infrastructure.redis;

import dev.gutemberg.rate.limiter.domain.enums.Action;
import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class RedisRateLimitRepository implements RateLimitRepository {
    private final RedisTemplate<String, RateLimit> redisTemplate;

    public RedisRateLimitRepository(final RedisTemplate<String, RateLimit> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<RateLimit> findByActionAndResource(final Action action, final String resource) {
        final var key = action + ":" + resource;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
