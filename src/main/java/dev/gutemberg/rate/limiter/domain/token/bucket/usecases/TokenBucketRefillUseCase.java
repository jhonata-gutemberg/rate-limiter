package dev.gutemberg.rate.limiter.domain.token.bucket.usecases;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketRefill;
import dev.gutemberg.rate.limiter.domain.token.bucket.repositories.TokenBucketRepository;

public class TokenBucketRefillUseCase {
    private final TokenBucketRepository tokenBucketRepository;

    public TokenBucketRefillUseCase(final TokenBucketRepository tokenBucketRepository) {
        this.tokenBucketRepository = tokenBucketRepository;
    }

    public void perform(final TokenBucketRefill refill) {
        final var optionalTokenBucket = tokenBucketRepository.findOneByConfigKeyAndIdentifier(
                refill.configKey(),
                refill.identifier()
        );
        if (optionalTokenBucket.isEmpty()) {
            return;
        }
        final var tokenBucket = optionalTokenBucket.get();
        tokenBucket.refill(refill);
    }
}
