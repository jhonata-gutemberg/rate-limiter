package dev.gutemberg.rate.limiter.domain.rate.limit.models.usecases;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig.Limit.Unit;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record ApplyRateLimitUseCaseOutput(Allowed allowed, Denied denied) {
    private ApplyRateLimitUseCaseOutput(Allowed allowed) {
        this(allowed, null);
    }

    private ApplyRateLimitUseCaseOutput(Denied denied) {
        this(null, denied);
    }

    public static ApplyRateLimitUseCaseOutput allow(Set<Allowed.Info> data) {
        return new ApplyRateLimitUseCaseOutput(new Allowed(data));
    }

    public static ApplyRateLimitUseCaseOutput allow() {
        return new ApplyRateLimitUseCaseOutput(new Allowed());
    }

    public static ApplyRateLimitUseCaseOutput deny(final Instant nextRefillAt) {
        return new ApplyRateLimitUseCaseOutput(new Denied(nextRefillAt));
    }

    public boolean isAllowed() {
        return allowed != null;
    }

    public record Allowed(Set<Info> data) {
        public Allowed() {
            this(Set.of());
        }

        public static DataBuilder dataBuilder() {
            return new DataBuilder();
        }

        public record Info(Unit unit, int requestsPerUnit, int requestsAvailable) {}

        public static class DataBuilder {
            final Set<Info> data = new HashSet<>();

            public void add(Unit unit, int requestsPerUnit, int requestsAvailable) {
                data.add(new Info(unit, requestsPerUnit, requestsAvailable));
            }

            public Set<Info> build() {
                return data;
            }
        }
    }

    public record Denied(Instant nextRefillAt) {}
}
