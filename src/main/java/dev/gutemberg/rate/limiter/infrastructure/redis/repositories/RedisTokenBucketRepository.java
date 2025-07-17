package dev.gutemberg.rate.limiter.infrastructure.redis.repositories;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.token.bucket.repositories.TokenBucketRepository;
import dev.gutemberg.rate.limiter.infrastructure.redis.models.TokenBucketValue;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Repository
public class RedisTokenBucketRepository implements TokenBucketRepository {
    private final RedisTemplate<String, TokenBucketValue> redisTemplate;

    public RedisTokenBucketRepository(final RedisTemplate<String, TokenBucketValue> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<TokenBucket> findOneByKey(final String key) {
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(key))
                .map(toTokenBucket(key));
    }

    @Override
    public void save(final TokenBucket tokenBucket) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(saveAsync(tokenBucket));
        }
    }

    private Function<TokenBucketValue, TokenBucket> toTokenBucket(final String key) {
        return value -> new TokenBucket(key, value.availableTokens());
    }

    private Runnable saveAsync(final TokenBucket tokenBucket) {
        return () -> this.redisTemplate.opsForValue()
                .set(tokenBucket.key(), TokenBucketValue.from(tokenBucket));
    }
}
