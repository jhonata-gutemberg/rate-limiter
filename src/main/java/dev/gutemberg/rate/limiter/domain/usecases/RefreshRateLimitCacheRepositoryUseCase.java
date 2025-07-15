package dev.gutemberg.rate.limiter.domain.usecases;

import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCacheRepository;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Set;

@Service
public class RefreshRateLimitCacheRepositoryUseCase {
    private final RateLimitRepository rateLimitRepository;
    private final RateLimitCacheRepository rateLimitCacheRepository;

    public RefreshRateLimitCacheRepositoryUseCase(
            final RateLimitRepository rateLimitRepository,
            final RateLimitCacheRepository rateLimitCacheRepository
    ) {
        this.rateLimitRepository = rateLimitRepository;
        this.rateLimitCacheRepository = rateLimitCacheRepository;
    }

    public void perform() {
        final Map<RateLimitCollectionKey, Set<RateLimit>> rateLimitCollections = rateLimitRepository.findAll();
        rateLimitCacheRepository.save(rateLimitCollections);
    }
}
