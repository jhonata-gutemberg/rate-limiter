package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;
import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;
import java.util.Map;

public record RateLimitRequest(RateLimitRequestAction action, String resource, Map<LimitedBy, String> identifiers) {
}
