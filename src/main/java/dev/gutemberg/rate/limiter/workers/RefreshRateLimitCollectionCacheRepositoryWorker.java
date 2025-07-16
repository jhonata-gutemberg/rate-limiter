package dev.gutemberg.rate.limiter.workers;

import dev.gutemberg.rate.limiter.domain.usecases.RefreshRateLimitCollectionCacheRepositoryUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshRateLimitCollectionCacheRepositoryWorker {
    private final RefreshRateLimitCollectionCacheRepositoryUseCase refreshRateLimitCollectionCacheRepositoryUseCase;

    public RefreshRateLimitCollectionCacheRepositoryWorker(
            final RefreshRateLimitCollectionCacheRepositoryUseCase refreshRateLimitCollectionCacheRepositoryUseCase
    ) {
        this.refreshRateLimitCollectionCacheRepositoryUseCase = refreshRateLimitCollectionCacheRepositoryUseCase;
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void schedule() {
        refreshRateLimitCollectionCacheRepositoryUseCase.perform();
    }
}
