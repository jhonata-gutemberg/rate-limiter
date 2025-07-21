package dev.gutemberg.rate.limiter.domain.token.bucket.models;

public record TokenBucketRefill(String configKey, String identifier, int refillRate) {
}
