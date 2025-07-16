package dev.gutemberg.rate.limiter.domain.models;

import java.util.Set;

public record RateLimitConfig(String key, Set<Limit> limits) {
    public record Limit(By by, Unit unit, int requestsPerUnit) {
        public enum By {
            IDENTIFIER,
            IP
        }

        public enum Unit {
            SECOND,
            MINUTE,
            HOUR,
            DAY
        }
    }
}
