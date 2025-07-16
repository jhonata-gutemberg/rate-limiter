package dev.gutemberg.rate.limiter.infrastructure.repositories.redis;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitConfigCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
public class RedisRateLimitConfigCacheRepository implements RateLimitConfigCacheRepository {
    private final RedisTemplate<String, RateLimitConfig.Limit> redisTemplate;

    public RedisRateLimitConfigCacheRepository(final RedisTemplate<String, RateLimitConfig.Limit> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<RateLimitConfig> findOneByKey(final String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().members(key)).flatMap(getConfig(key));
    }

    @Override
    public void save(final Set<RateLimitConfig> configs) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            configs.forEach(submit(executor));
        }
    }

    private Function<Set<RateLimitConfig.Limit>, Optional<RateLimitConfig>> getConfig(final String key) {
        return values -> values.isEmpty() ? Optional.empty() : Optional.of(new RateLimitConfig(key, values));
    }

    private Consumer<RateLimitConfig> submit(final ExecutorService executor) {
        return config -> executor.submit(save(config));
    }

    private Runnable save(final RateLimitConfig config) {
        return () -> {
            final var key = config.key();
            redisTemplate.delete(key);
            config.limits().forEach(limit -> redisTemplate.opsForSet().add(key, limit));
        };
    }
}
