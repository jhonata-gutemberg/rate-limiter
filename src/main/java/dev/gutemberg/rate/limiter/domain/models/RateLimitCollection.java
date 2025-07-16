package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;
import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;
import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import java.util.Set;

public record RateLimitCollection(Key key, Set<Value> values) {
    public record Key(RateLimitRequestAction action, String resource) {
        public static Key from(final RateLimitRequest request) {
            return new Key(request.action(), request.resource());
        }
    }

    public record Value(LimitedBy limitedBy, RateUnit unit, int requestsPerUnit) {
    }
}
