package dev.gutemberg.rate.limiter.workers;

import dev.gutemberg.rate.limiter.domain.usecases.RefreshRateLimitCacheRepositoryUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshRateLimitCacheRepositoryWorker {
    private final RefreshRateLimitCacheRepositoryUseCase refreshRateLimitCacheRepositoryUseCase;

    public RefreshRateLimitCacheRepositoryWorker(
            final RefreshRateLimitCacheRepositoryUseCase refreshRateLimitCacheRepositoryUseCase
    ) {
        this.refreshRateLimitCacheRepositoryUseCase = refreshRateLimitCacheRepositoryUseCase;
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void schedule() {
        refreshRateLimitCacheRepositoryUseCase.perform();
    }
}
