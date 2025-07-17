package dev.gutemberg.rate.limiter.infrastructure.repositories.redis;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.token.bucket.repositories.TokenBucketRepository;
import dev.gutemberg.rate.limiter.infrastructure.models.TokenBucketValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.Executors;

@Repository
public class RedisTokenBucketRepository implements TokenBucketRepository {
    private final RedisTemplate<String, TokenBucketValue> redisTemplate;
    private final Converter<Pair<String, TokenBucketValue>, TokenBucket> keyValuePairToTokenBucketConverter;

    public RedisTokenBucketRepository(
            final RedisTemplate<String, TokenBucketValue> redisTemplate,
            final Converter<Pair<String, TokenBucketValue>, TokenBucket> keyValuePairToTokenBucketConverter
    ) {
        this.redisTemplate = redisTemplate;
        this.keyValuePairToTokenBucketConverter = keyValuePairToTokenBucketConverter;
    }

    @Override
    public Optional<TokenBucket> findOneByKey(final String key) {
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(key))
                .map(value ->
                        keyValuePairToTokenBucketConverter.convert(Pair.of(key, value)));
    }

    @Override
    public void save(final TokenBucket tokenBucket) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(saveAsync(tokenBucket));
        }
    }

    private Runnable saveAsync(final TokenBucket tokenBucket) {
        return () -> this.redisTemplate.opsForValue()
                .set(tokenBucket.key(), TokenBucketValue.from(tokenBucket));
    }
}
