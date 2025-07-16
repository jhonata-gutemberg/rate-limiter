package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.*;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCollectionCacheRepository;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import org.springframework.stereotype.Service;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static dev.gutemberg.rate.limiter.domain.models.RateLimitResponse.*;

@Service
public class ApplyRateLimitUseCase {
    private final RateLimitCollectionCacheRepository rateLimitCollectionCacheRepository;
    private final TokenBucketRepository tokenBucketRepository;

    public ApplyRateLimitUseCase(
            final RateLimitCollectionCacheRepository rateLimitCollectionCacheRepository,
            final TokenBucketRepository tokenBucketRepository
    ) {
        this.rateLimitCollectionCacheRepository = rateLimitCollectionCacheRepository;
        this.tokenBucketRepository = tokenBucketRepository;
    }

    public RateLimitResponse perform(final RateLimitRequest request) {
        return rateLimitCollectionCacheRepository.findOneByKey(RateLimitCollection.Key.from(request))
                .map(allowOrDenyRequest(request))
                .orElse(allowRequest());
    }

    private Function<RateLimitCollection, RateLimitResponse> allowOrDenyRequest(final RateLimitRequest request) {
        return collection -> {
            final Map<RateUnit, Integer> remainingRequests = new EnumMap<>(RateUnit.class);
            try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (final var rateLimit: collection.values()) {
                    final var rateUnit = rateLimit.unit();
                    final var tokenBucket = getTokenBucket(collection.key(), rateLimit, request);
                    if (!tokenBucket.hasAvailableTokens()) {
                        return denyRequest(rateUnit);
                    }
                    executor.submit(consumeToken(tokenBucket, remainingRequests, rateUnit));
                }
            }
            return allowRequest(collection, remainingRequests);
        };
    }

    private TokenBucket getTokenBucket(
            final RateLimitCollection.Key collectionKey,
            final RateLimitCollection.Value rateLimit,
            final RateLimitRequest request
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
