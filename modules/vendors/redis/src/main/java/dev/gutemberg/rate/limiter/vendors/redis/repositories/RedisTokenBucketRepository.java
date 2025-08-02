package dev.gutemberg.rate.limiter.vendors.redis.repositories;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.token.bucket.contracts.repositories.TokenBucketRepository;
import dev.gutemberg.rate.limiter.vendors.redis.contracts.RedisTemplate;
import dev.gutemberg.rate.limiter.vendors.redis.contracts.RedisTemplateFactory;
import dev.gutemberg.rate.limiter.vendors.redis.models.TokenBucketValue;
import jakarta.inject.Named;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Named
public class RedisTokenBucketRepository implements TokenBucketRepository {
    private final RedisTemplate<TokenBucketValue> redisTemplate;

    public RedisTokenBucketRepository(final RedisTemplateFactory redisTemplateFactory) {
        this.redisTemplate = redisTemplateFactory.getTemplate(TokenBucketValue.class);
    }

    @Override
    public Optional<TokenBucket> findOneByConfigKeyAndIdentifier(final String configKey, String identifier) {
        final Function<Object, TokenBucket> toTokenBucket = object -> {
            final var value = (TokenBucketValue) object;
            return new TokenBucket(identifier, value.availableTokens(), value.nextRefillAt());
        };
        final var value = this.redisTemplate.opsForHash().entries(configKey).get(identifier);
        return Optional.ofNullable(value).map(toTokenBucket);
    }

    @Override
    public void save(final String configKey, final TokenBucket tokenBucket) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final Runnable saveAsync = () -> this.redisTemplate.opsForHash()
                    .put(configKey, tokenBucket.identifier(), TokenBucketValue.from(tokenBucket));
            executor.submit(saveAsync);
        }
    }
}
