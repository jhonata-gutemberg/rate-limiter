package dev.gutemberg.rate.limiter.infrastructure.s3.models;

import java.util.Objects;
import java.util.Set;

public record RateLimitConfigFromFile(String action, String resource, Set<Limit> limits) {
    public record Limit(String by, String unit, int requests_per_unit) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Limit limit)) return false;
            return Objects.equals(by, limit.by) && Objects.equals(unit, limit.unit);
        }

        @Override
        public int hashCode() {
            return Objects.hash(by, unit);
        }
    }
}
