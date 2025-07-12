package dev.gutemberg.rate.limiter.infrastructure.repositories;

import dev.gutemberg.rate.limiter.domain.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.models.TokenBucketKey;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class RedisTokenBucketRepository implements TokenBucketRepository {
    private final RedisTemplate<String, TokenBucket> redisTemplate;

    public RedisTokenBucketRepository(final RedisTemplate<String, TokenBucket> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<TokenBucket> findOneByKey(final TokenBucketKey tokenBucketKey) {
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(tokenBucketKey.toString()))
                .map(tokenBucket -> tokenBucket.setKey(tokenBucketKey));
    }

    @Override
    public void save(final TokenBucket tokenBucket) {
        this.redisTemplate.opsForValue().set(tokenBucket.getKey().toString(), tokenBucket);
    }
}
