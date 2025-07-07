package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;

public record RateLimit(LimitedBy limitedBy, String unit, int requestsPerUnit) {
}
