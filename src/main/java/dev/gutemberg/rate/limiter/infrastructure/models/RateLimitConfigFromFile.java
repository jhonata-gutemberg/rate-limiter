package dev.gutemberg.rate.limiter.infrastructure.models;

import java.util.Set;

public record RateLimitConfigFromFile(String action, String resource, Set<Limit> limits) {
    public record Limit(String by, String unit, int requests_per_unit) {}
}
