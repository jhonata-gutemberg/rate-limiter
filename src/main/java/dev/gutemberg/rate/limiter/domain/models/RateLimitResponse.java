package dev.gutemberg.rate.limiter.domain.models;

import java.util.Map;
import java.util.Set;

import static dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.*;
import static dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit.*;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static java.util.stream.Collectors.toMap;

public record RateLimitResponse(RequestAllowed requestAllowed, RequestDenied requestDenied) {
    private RateLimitResponse(
            final Set<Unit> rateUnits,
            final Map<Unit, Integer> remainingRequests,
            final Map<Unit, Integer> requestsLimit
    ) {
        this(new RequestAllowed(rateUnits, remainingRequests, requestsLimit), null);
    }

    private RateLimitResponse(final Unit unit) {
        this(null, new RequestDenied(60));
    }

    public static RateLimitResponse allowRequest(
            final RateLimitConfig config,
            final Map<Unit, Integer> remainingRequests
    ) {
        final var rateUnits = config.limits()
                .stream()
                .map(Limit::unit)
                .collect(toUnmodifiableSet());
        final var requestsLimit = config.limits()
                .stream()
                .collect(toMap(Limit::unit, Limit::requestsPerUnit));
        return new RateLimitResponse(rateUnits, remainingRequests, requestsLimit);
    }

    public static RateLimitResponse allowRequest() {
        return new RateLimitResponse(Set.of(), Map.of(), Map.of());
    }

    public static RateLimitResponse denyRequest(final Unit unit) {
        return new RateLimitResponse(unit);
    }

    public boolean hasRequestDenied() {
        return requestDenied != null;
    }

    public record RequestAllowed(
            Set<Unit> rateUnits,
            Map<Unit, Integer> remainingRequests,
            Map<Unit, Integer> requestsLimit
    ) {
    }

    public record RequestDenied(int retryAfterInSeconds) {
    }
}
