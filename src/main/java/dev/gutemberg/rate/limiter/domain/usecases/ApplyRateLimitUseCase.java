package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.*;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import dev.gutemberg.rate.limiter.domain.repositories.TokenBucketRepository;
import org.springframework.stereotype.Service;
import java.util.EnumMap;
import java.util.Map;

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
        for (final var rateLimit: rateLimits) {
            final var rateUnit = rateLimit.unit();
            final var identifier = request.identifiers().get(rateLimit.limitedBy());
            final var tokenBucketKey = new TokenBucketKey(collectionKey, identifier, rateUnit);
            final var tokenBucket = tokenBucketRepository.findOneByKey(tokenBucketKey)
                    .orElseGet(() -> new TokenBucket(tokenBucketKey, rateLimit.requestsPerUnit()));
            if (!tokenBucket.hasAvailableTokens()) {
                return RateLimitResponse.denyRequest(rateUnit);
            }
            tokenBucket.consumeToken();
            remainingRequests.put(rateUnit, tokenBucket.availableTokens());
            tokenBucketRepository.save(tokenBucket); //async
        }
        return RateLimitResponse.allowRequest(rateLimits, remainingRequests);
    }
}
