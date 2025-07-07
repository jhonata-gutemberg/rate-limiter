package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.enums.Action;
import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import java.util.Optional;

public interface RateLimitRepository {
    Optional<RateLimit> findByActionAndResource(Action action, String resource);
}
