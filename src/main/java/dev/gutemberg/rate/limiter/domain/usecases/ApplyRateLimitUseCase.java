package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfigKeyBuilder;
import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit.By;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.ApplyRateLimitUseCaseInput;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.ApplyRateLimitUseCaseOutput;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.ApplyRateLimitUseCaseOutput.Allowed;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitConfigCacheRepository;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketKeyBuilder;
import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class ApplyRateLimitUseCase {
    private final RateLimitConfigCacheRepository rateLimitConfigCacheRepository;
    private final TokenBucketRepository tokenBucketRepository;

    public ApplyRateLimitUseCase(
            final RateLimitConfigCacheRepository rateLimitConfigCacheRepository,
            final TokenBucketRepository tokenBucketRepository
    ) {
        this.rateLimitConfigCacheRepository = rateLimitConfigCacheRepository;
        this.tokenBucketRepository = tokenBucketRepository;
    }

    public ApplyRateLimitUseCaseOutput perform(final ApplyRateLimitUseCaseInput input) {
        return rateLimitConfigCacheRepository.findOneByKey(RateLimitConfigKeyBuilder.build(input))
                .map(toOutput(input))
                .orElseGet(ApplyRateLimitUseCaseOutput::allow);
    }

    private Function<RateLimitConfig, ApplyRateLimitUseCaseOutput> toOutput(final ApplyRateLimitUseCaseInput input) {
        return config -> {
            final var dataBuilder = Allowed.dataBuilder();
            for (final var limit: config.limits()) {
                final var tokenBucket = getTokenBucket(config.key(), limit, input.identifiers());
                if (!tokenBucket.hasAvailableTokens()) {
                    return ApplyRateLimitUseCaseOutput.deny();
                }
                consumeToken(tokenBucket);
                dataBuilder.add(limit.unit(), limit.requestsPerUnit(), tokenBucket.availableTokens());
            }
            return ApplyRateLimitUseCaseOutput.allow(dataBuilder.build());
        };
    }

    private TokenBucket getTokenBucket(
            final String configKey,
            final Limit limit,
            final Map<By, String> identifiers
    ) {
        final var identifier = identifiers.get(limit.by());
        final var key = TokenBucketKeyBuilder.build(configKey, identifier, limit.unit());
        return tokenBucketRepository.findOneByKey(key)
                .orElseGet(() -> new TokenBucket(key, limit.requestsPerUnit()));
    }

    private void consumeToken(final TokenBucket tokenBucket) {
        tokenBucket.consumeToken();
        tokenBucketRepository.save(tokenBucket);
    }
}
