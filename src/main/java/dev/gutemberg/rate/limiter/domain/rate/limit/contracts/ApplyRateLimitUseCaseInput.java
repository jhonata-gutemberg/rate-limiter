package dev.gutemberg.rate.limiter.domain.rate.limit.contracts;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.By;
import java.util.Map;

public record ApplyRateLimitUseCaseInput(Action action, String resource, Map<By, String> identifiers) {
    public enum Action {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        UNKNOWN
    }
}
