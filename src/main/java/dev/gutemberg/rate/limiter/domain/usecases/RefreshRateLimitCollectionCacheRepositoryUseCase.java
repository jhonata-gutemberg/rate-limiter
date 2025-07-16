package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCollectionCacheRepository;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshRateLimitCollectionCacheRepositoryUseCase {
    private final RateLimitCollectionRepository rateLimitCollectionRepository;
    private final RateLimitCollectionCacheRepository rateLimitCollectionCacheRepository;

    public RefreshRateLimitCollectionCacheRepositoryUseCase(
            final RateLimitCollectionRepository rateLimitCollectionRepository,
            final RateLimitCollectionCacheRepository rateLimitCollectionCacheRepository
    ) {
        this.rateLimitCollectionRepository = rateLimitCollectionRepository;
        this.rateLimitCollectionCacheRepository = rateLimitCollectionCacheRepository;
    }

    public void perform() {
        final var collections = rateLimitCollectionRepository.findAll();
        rateLimitCollectionCacheRepository.save(collections);
    }
}
