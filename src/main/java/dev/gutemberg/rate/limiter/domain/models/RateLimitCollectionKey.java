package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;

public record RateLimitCollectionKey(RateLimitRequestAction action, String resource) {
}
