package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static java.util.stream.Collectors.toMap;

public record RateLimitResponse(RequestAllowed requestAllowed, RequestDenied requestDenied) {
    private RateLimitResponse(
            final Set<RateUnit> rateUnits,
            final Map<RateUnit, Integer> remainingRequests,
            final Map<RateUnit, Integer> requestsLimit
    ) {
        this(new RequestAllowed(rateUnits, remainingRequests, requestsLimit), null);
    }

    private RateLimitResponse(final RateUnit rateUnit) {
        this(null, new RequestDenied(rateUnit.valueInSeconds()));
    }

    public static RateLimitResponse allowRequest(
            final Set<RateLimit> rateLimits,
            final Map<RateUnit, Integer> remainingRequests
    ) {
        final var rateUnits = rateLimits.stream().map(RateLimit::unit).collect(toUnmodifiableSet());
        final var requestsLimit = rateLimits.stream().collect(toMap(RateLimit::unit, RateLimit::requestsPerUnit));
        return new RateLimitResponse(rateUnits, remainingRequests, requestsLimit);
    }

    public static RateLimitResponse denyRequest(final RateUnit rateUnit) {
        return new RateLimitResponse(rateUnit);
    }

    public boolean hasRequestDenied() {
        return requestDenied != null;
    }

    public record RequestAllowed(
            Set<RateUnit> rateUnits,
            Map<RateUnit, Integer> remainingRequests,
            Map<RateUnit, Integer> requestsLimit
    ) {
    }

    public record RequestDenied(int retryAfterInSeconds) {
    }
}
