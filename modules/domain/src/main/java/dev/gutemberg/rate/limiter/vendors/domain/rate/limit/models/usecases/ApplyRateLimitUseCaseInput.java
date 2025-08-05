package dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.usecases;

import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig.Limit.By;
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
