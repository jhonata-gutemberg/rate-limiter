package dev.gutemberg.rate.limiter.infra.jobrunr;

import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.schedulers.TokenBucketRefillScheduler;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.Unit;
import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucketRefill;
import dev.gutemberg.rate.limiter.domain.token.bucket.usecases.TokenBucketRefillUseCase;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.stereotype.Component;
import java.util.Map;

import static dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.Unit.*;

@Component
public class JobRunrTokenBucketRefillScheduler implements TokenBucketRefillScheduler {
    private static final Map<Unit, String> CRON = Map.of(
            SECOND, "* * * * * *",
            MINUTE, Cron.minutely(),
            HOUR, Cron.hourly(),
            DAY, Cron.daily()
    );

    private final JobScheduler jobScheduler;
    private final TokenBucketRefillUseCase tokenBucketRefillUseCase;

    public JobRunrTokenBucketRefillScheduler(
            final JobScheduler jobScheduler,
            final TokenBucketRefillUseCase tokenBucketRefillUseCase
    ) {
        this.jobScheduler = jobScheduler;
        this.tokenBucketRefillUseCase = tokenBucketRefillUseCase;
    }

    @Override
    public void schedule(final TokenBucketRefill refill) {
        jobScheduler.scheduleRecurrently(CRON.get(refill.unit()), () -> tokenBucketRefillUseCase.perform(refill));
    }
}
