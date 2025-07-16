package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.*;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCollectionCacheRepository;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import org.springframework.stereotype.Service;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

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
        final Map<RateUnit, Integer> remainingRequests = new EnumMap<>(RateUnit.class);
        final var collectionKey = new RateLimitCollection.Key(request.action(), request.resource());
        final var optionalCollection = rateLimitCollectionCacheRepository.findOneByKey(collectionKey);
        if (optionalCollection.isPresent()) {
            final var collection = optionalCollection.get();
            final var values = collection.values();
            try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (final var rateLimit: values) {
                    final var rateUnit = rateLimit.unit();
                    final var tokenBucket = getTokenBucket(collectionKey, rateLimit, request);
                    if (!tokenBucket.hasAvailableTokens()) {
                        return denyRequest(rateUnit);
                    }
                    executor.submit(consumeToken(tokenBucket, remainingRequests, rateUnit));
                }
            }
            return allowRequest(values, remainingRequests);
        }
        return allowRequest(Set.of(), remainingRequests);
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
