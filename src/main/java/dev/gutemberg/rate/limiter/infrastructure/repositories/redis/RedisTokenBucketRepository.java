package dev.gutemberg.rate.limiter.infrastructure.repositories.redis;

import dev.gutemberg.rate.limiter.domain.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.models.TokenBucketKey;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import dev.gutemberg.rate.limiter.infrastructure.models.TokenBucketValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class RedisTokenBucketRepository implements TokenBucketRepository {
    private final RedisTemplate<String, TokenBucketValue> redisTemplate;
    private final Converter<Pair<TokenBucketKey, TokenBucketValue>, TokenBucket> keyValuePairToTokenBucketConverter;

    public RedisTokenBucketRepository(
            final RedisTemplate<String, TokenBucketValue> redisTemplate,
            final Converter<Pair<TokenBucketKey, TokenBucketValue>, TokenBucket> keyValuePairToTokenBucketConverter
    ) {
        this.redisTemplate = redisTemplate;
        this.keyValuePairToTokenBucketConverter = keyValuePairToTokenBucketConverter;
    }

    @Override
    public Optional<TokenBucket> findOneByKey(final TokenBucketKey tokenBucketKey) {
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(tokenBucketKey.toString()))
                .map(value ->
                        keyValuePairToTokenBucketConverter.convert(Pair.of(tokenBucketKey, value)));
    }

    @Override
    public void save(final TokenBucket tokenBucket) {
        this.redisTemplate.opsForValue().set(tokenBucket.key().toString(), TokenBucketValue.from(tokenBucket));
    }
}
