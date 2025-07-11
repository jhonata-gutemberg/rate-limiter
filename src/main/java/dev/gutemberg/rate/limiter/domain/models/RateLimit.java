package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;
import dev.gutemberg.rate.limiter.domain.enums.RateUnit;

public record RateLimit(LimitedBy limitedBy, RateUnit unit, int requestsPerUnit) {
}
