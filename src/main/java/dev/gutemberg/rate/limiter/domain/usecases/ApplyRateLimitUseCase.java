package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitRequest;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.models.RateLimitResponse;
import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.models.TokenBucketKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import org.springframework.stereotype.Service;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Service
public class ApplyRateLimitUseCase {
    private final RateLimitRepository rateLimitRepository;
    private final TokenBucketRepository tokenBucketRepository;

    public ApplyRateLimitUseCase(
            final RateLimitRepository rateLimitRepository,
            final TokenBucketRepository tokenBucketRepository
    ) {
        this.rateLimitRepository = rateLimitRepository;
        this.tokenBucketRepository = tokenBucketRepository;
    }

    public RateLimitResponse perform(final RateLimitRequest request) {
        final Map<RateUnit, Integer> remainingRequests = new EnumMap<>(RateUnit.class);
        final var collectionKey = new RateLimitCollectionKey(request.action(), request.resource());
        final var rateLimits = rateLimitRepository.findAllByCollectionKey(collectionKey);
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (final var rateLimit: rateLimits) {
                final var rateUnit = rateLimit.unit();
                final var tokenBucket = getTokenBucket(rateLimit, request, collectionKey);
                if (!tokenBucket.hasAvailableTokens()) {
                    return RateLimitResponse.denyRequest(rateUnit);
                }
                executor.submit(consumeToken(tokenBucket, remainingRequests, rateUnit));
            }
        }
        return RateLimitResponse.allowRequest(rateLimits, remainingRequests);
    }

    private TokenBucket getTokenBucket(
            final RateLimit rateLimit,
            RateLimitRequest request,
            RateLimitCollectionKey collectionKey
    ) {
        final var identifier = request.identifiers().get(rateLimit.limitedBy());
        final var tokenBucketKey = new TokenBucketKey(collectionKey, identifier, rateLimit.unit());
        return tokenBucketRepository.findOneByKey(tokenBucketKey)
                .orElseGet(() -> new TokenBucket(tokenBucketKey, rateLimit.requestsPerUnit()));
    }

    private Runnable consumeToken(
            final TokenBucket tokenBucket,
            final Map<RateUnit, Integer> remainingRequests,
            RateUnit rateUnit
    ) {
        return () -> {
            tokenBucket.consumeToken();
            remainingRequests.put(rateUnit, tokenBucket.availableTokens());
            tokenBucketRepository.save(tokenBucket);
        };
    }
}
