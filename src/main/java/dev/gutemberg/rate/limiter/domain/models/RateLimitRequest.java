package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.Action;
import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;
import java.util.Map;

public record RateLimitRequest(Action action, String resource, Map<LimitedBy, String> identifiers) {
}
