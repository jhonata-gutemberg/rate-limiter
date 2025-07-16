package dev.gutemberg.rate.limiter.workers;

import dev.gutemberg.rate.limiter.domain.usecases.RefreshRateLimitConfigCacheRepositoryUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshRateLimitConfigCacheRepositoryWorker {
    private final RefreshRateLimitConfigCacheRepositoryUseCase refreshRateLimitConfigCacheRepositoryUseCase;

    public RefreshRateLimitConfigCacheRepositoryWorker(
            final RefreshRateLimitConfigCacheRepositoryUseCase refreshRateLimitConfigCacheRepositoryUseCase
    ) {
        this.refreshRateLimitConfigCacheRepositoryUseCase = refreshRateLimitConfigCacheRepositoryUseCase;
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void schedule() {
        refreshRateLimitConfigCacheRepositoryUseCase.perform();
    }
}
