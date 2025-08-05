package dev.gutemberg.rate.limiter.vendors.redis.repositories;

import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig;
import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.contracts.repositories.RateLimitConfigCacheRepository;
import dev.gutemberg.rate.limiter.vendors.redis.contracts.RedisTemplate;
import dev.gutemberg.rate.limiter.vendors.redis.contracts.RedisTemplateFactory;
import jakarta.inject.Named;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig.Limit;

@Named
public class RedisRateLimitConfigCacheRepository implements RateLimitConfigCacheRepository {
    private final RedisTemplate<Limit> redisTemplate;

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
