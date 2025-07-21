package dev.gutemberg.rate.limiter.domain.rate.limit.models;

import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Set;

public record RateLimitConfig(String key, Set<Limit> limits) {
    public static class KeyBuilder {
        private KeyBuilder() {}

        public static String build(final ApplyRateLimitUseCaseInput input) {
            return input.action().name().toLowerCase() + ":" + input.resource();
        }
    }

    public record Limit(By by, Unit unit, int requestsPerUnit) {
        public enum By {
            IDENTIFIER,
            IP
        }

        public enum Unit {
            SECOND(ChronoUnit.SECONDS),
            MINUTE(ChronoUnit.MINUTES),
            HOUR(ChronoUnit.HOURS),
            DAY(ChronoUnit.DAYS);

            private final TemporalUnit temporal;

            Unit(final TemporalUnit temporal) {
                this.temporal = temporal;
            }

            public TemporalUnit temporal() {
                return temporal;
            }
        }
    }
}
