package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.Action;

public record RateLimitCollectionKey(Action action, String resource) {
    @Override
    public String toString() {
        return action + ":" + resource;
    }
}
