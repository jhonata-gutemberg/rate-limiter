package dev.gutemberg.rate.limiter.vendors.domain.token.bucket.usecases;

import dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities.TokenBucketRefill;
import dev.gutemberg.rate.limiter.vendors.domain.token.bucket.contracts.repositories.TokenBucketRepository;
import jakarta.inject.Named;

@Named
public class TokenBucketRefillUseCase {
    private final TokenBucketRepository tokenBucketRepository;

    public TokenBucketRefillUseCase(final TokenBucketRepository tokenBucketRepository) {
        this.tokenBucketRepository = tokenBucketRepository;
    }

    public void perform(final TokenBucketRefill refill) {
        final var configKey = refill.configKey();
        final var optionalTokenBucket = tokenBucketRepository.findOneByConfigKeyAndIdentifier(
                configKey,
                refill.identifier()
        );
        if (optionalTokenBucket.isEmpty()) {
            return;
        }
        final var tokenBucket = optionalTokenBucket.get();
        tokenBucket.refill(refill);
        tokenBucketRepository.save(configKey, tokenBucket);
    }
}
