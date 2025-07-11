package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.Action;

public record TokenBucketCollectionKey(Action action, String resource, String limitIdentifier) {
}
