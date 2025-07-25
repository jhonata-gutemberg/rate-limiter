package dev.gutemberg.rate.limiter.infrastructure.redis.repositories;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories.RateLimitConfigCacheRepository;
import dev.gutemberg.rate.limiter.infrastructure.redis.contracts.RedisTemplateFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit;

@Repository
public class RedisRateLimitConfigCacheRepository implements RateLimitConfigCacheRepository {
    private final RedisTemplate<String, Limit> redisTemplate;

    public RedisRateLimitConfigCacheRepository(final RedisTemplateFactory redisTemplateFactory) {
        this.redisTemplate = redisTemplateFactory.getTemplate(Limit.class);
    }

    @Override
    public Optional<RateLimitConfig> findOneByKey(final String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().members(key)).flatMap(getConfig(key));
    }

    @Override
    public void save(final Set<RateLimitConfig> configs) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final Consumer<RateLimitConfig> submit = config -> executor.submit(save(config));
            configs.forEach(submit);
        }
    }

    private Function<Set<Limit>, Optional<RateLimitConfig>> getConfig(final String key) {
        return values -> values.isEmpty() ? Optional.empty() : Optional.of(new RateLimitConfig(key, values));
    }

    private Runnable save(final RateLimitConfig config) {
        return () -> config.limits().forEach(limit -> redisTemplate.opsForSet().add(config.key(), limit));
    }
}
