package dev.gutemberg.rate.limiter.domain.rate.limit.models;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import java.util.Map;

public record ApplyRateLimitUseCaseInput(Action action, String resource, Map<RateLimitConfig.Limit.By, String> identifiers) {
    public enum Action {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        UNKNOWN
    }
}
