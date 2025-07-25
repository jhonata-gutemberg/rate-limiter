package dev.gutemberg.rate.limiter.domain.token.bucket.usecases;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketRefill;
import dev.gutemberg.rate.limiter.domain.token.bucket.repositories.TokenBucketRepository;
import org.springframework.stereotype.Service;

@Service
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
