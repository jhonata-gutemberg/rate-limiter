package dev.gutemberg.rate.limiter.domain.rate.limit.usecases;

import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories.RateLimitConfigCacheRepository;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories.RateLimitConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshRateLimitConfigCacheRepositoryUseCase {
    private final RateLimitConfigRepository rateLimitConfigRepository;
    private final RateLimitConfigCacheRepository rateLimitConfigCacheRepository;

    public RefreshRateLimitConfigCacheRepositoryUseCase(
            final RateLimitConfigRepository rateLimitConfigRepository,
            final RateLimitConfigCacheRepository rateLimitConfigCacheRepository
    ) {
        this.rateLimitConfigRepository = rateLimitConfigRepository;
        this.rateLimitConfigCacheRepository = rateLimitConfigCacheRepository;
    }

    public void perform() {
        final var configs = rateLimitConfigRepository.findAll();
        rateLimitConfigCacheRepository.save(configs);
    }
}
