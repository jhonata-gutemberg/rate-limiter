package dev.gutemberg.rate.limiter.domain.rate.limit.usecases;

import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.schedulers.TokenBucketRefillScheduler;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseOutput;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseOutput.Allowed;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories.RateLimitConfigCacheRepository;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit;
import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketConfig;
import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketRefill;
import dev.gutemberg.rate.limiter.domain.token.bucket.repositories.TokenBucketRepository;
import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import jakarta.inject.Named;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;

@Named
public class ApplyRateLimitUseCase {
    private final RateLimitConfigCacheRepository rateLimitConfigCacheRepository;
    private final TokenBucketRepository tokenBucketRepository;
    private final TokenBucketRefillScheduler tokenBucketRefillScheduler;

    public ApplyRateLimitUseCase(
            final RateLimitConfigCacheRepository rateLimitConfigCacheRepository,
            final TokenBucketRepository tokenBucketRepository,
            final TokenBucketRefillScheduler tokenBucketRefillScheduler
    ) {
        this.rateLimitConfigCacheRepository = rateLimitConfigCacheRepository;
        this.tokenBucketRepository = tokenBucketRepository;
        this.tokenBucketRefillScheduler = tokenBucketRefillScheduler;
    }

    public ApplyRateLimitUseCaseOutput perform(final ApplyRateLimitUseCaseInput input) {
        final var toOutput = allowOrDeny(input);
        return rateLimitConfigCacheRepository.findOneByKey(RateLimitConfig.KeyBuilder.build(input))
                .map(toOutput)
                .orElseGet(ApplyRateLimitUseCaseOutput::allow);
    }

    private Function<RateLimitConfig, ApplyRateLimitUseCaseOutput> allowOrDeny(final ApplyRateLimitUseCaseInput input) {
        return config -> {
            final var dataBuilder = Allowed.dataBuilder();
            for (final var limit: config.limits()) {
                final var tokenBucketConfigKey = TokenBucketConfig.KeyBuilder.build(config.key(), limit.unit());
                final var identifier = input.identifiers().get(limit.by());
                final var create = createTokenBucket(tokenBucketConfigKey, identifier, limit);
                final var tokenBucket = tokenBucketRepository
                        .findOneByConfigKeyAndIdentifier(tokenBucketConfigKey, identifier)
                        .orElseGet(create);
                if (!tokenBucket.hasAvailableTokens()) {
                    return ApplyRateLimitUseCaseOutput.deny(tokenBucket.nextRefillAt());
                }
                tokenBucket.consumeToken();
                tokenBucketRepository.save(tokenBucketConfigKey, tokenBucket);
                dataBuilder.add(limit.unit(), limit.requestsPerUnit(), tokenBucket.availableTokens());
            }
            return ApplyRateLimitUseCaseOutput.allow(dataBuilder.build());
        };
    }

    private Supplier<TokenBucket> createTokenBucket(final String configKey, final String identifier, final Limit limit) {
        return () -> {
            final var requestsPerUnit = limit.requestsPerUnit();
            final var unit = limit.unit();
            final var nextRefillAt = Instant.now().plus(1, unit.temporal());
            final var tokenBucket = new TokenBucket(identifier, requestsPerUnit, nextRefillAt);
            final var refill = new TokenBucketRefill(configKey, identifier, requestsPerUnit, unit);
            tokenBucketRepository.save(configKey, tokenBucket);
            tokenBucketRefillScheduler.schedule(refill);
            return tokenBucket;
        };
    }
}
