package dev.gutemberg.rate.limiter.domain.models;

public record RateLimit(String limitedBy, String unit, int requestsPerUnit) {
}
