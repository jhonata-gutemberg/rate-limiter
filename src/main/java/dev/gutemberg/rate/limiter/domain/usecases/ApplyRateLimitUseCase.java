package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.*;
import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit.Unit;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitConfigCacheRepository;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import org.springframework.stereotype.Service;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static dev.gutemberg.rate.limiter.domain.models.RateLimitResponse.*;

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

    public RateLimitResponse perform(final RateLimitRequest request) {
        final var key = request.action().name().toLowerCase() + ":" + request.resource();
        return rateLimitConfigCacheRepository.findOneByKey(key)
                .map(allowOrDenyRequest(request))
                .orElse(allowRequest());
    }

    private Function<RateLimitConfig, RateLimitResponse> allowOrDenyRequest(final RateLimitRequest request) {
        return config -> {
            final Map<Unit, Integer> remainingRequests = new EnumMap<>(Unit.class);
            try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (final var limit: config.limits()) {
                    final var unit = limit.unit();
                    final var tokenBucket = getTokenBucket(config.key(), limit, request);
                    if (!tokenBucket.hasAvailableTokens()) {
                        return denyRequest(unit);
                    }
                    executor.submit(consumeToken(tokenBucket, remainingRequests, unit));
                }
            }
            return allowRequest(config, remainingRequests);
        };
    }

    private TokenBucket getTokenBucket(
            final String configKey,
            final RateLimitConfig.Limit limit,
            final RateLimitRequest request
    ) {
        final var identifier = request.identifiers().get(limit.by());
        final var tokenBucketKey = new TokenBucketKey(configKey, identifier, limit.unit());
        return tokenBucketRepository.findOneByKey(tokenBucketKey)
                .orElseGet(() -> new TokenBucket(tokenBucketKey, limit.requestsPerUnit()));
    }

    private Runnable consumeToken(
            final TokenBucket tokenBucket,
            final Map<Unit, Integer> remainingRequests,
            Unit rateUnit
    ) {
        return () -> {
            tokenBucket.consumeToken();
            remainingRequests.put(rateUnit, tokenBucket.availableTokens());
            tokenBucketRepository.save(tokenBucket);
        };
    }
}
