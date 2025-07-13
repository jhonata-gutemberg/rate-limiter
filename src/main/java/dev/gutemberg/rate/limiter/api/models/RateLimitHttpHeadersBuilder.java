package dev.gutemberg.rate.limiter.api.models;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitResponse;
import org.springframework.http.HttpHeaders;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RateLimitHttpHeadersBuilder {
    private static final String RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER = "X-RateLimit-Retry-After-Seconds";
    private static final String RATE_LIMIT_REQUESTS_REMAINING_HEADER = "X-RateLimit-Requests-Remaining";
    private static final String RATE_LIMIT_REQUESTS_LIMIT_HEADER = "X-RateLimit-Requests-Limit";

    private RateLimitHttpHeadersBuilder() {}

    public static HttpHeaders denied(final RateLimitResponse.RequestDenied requestDenied) {
        final var headers = new HttpHeaders();
        headers.add(RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER, String.valueOf(requestDenied.retryAfterInSeconds()));
        return headers;
    }

    public static HttpHeaders allowed(final RateLimitResponse.RequestAllowed requestAllowed) {
        final var rateUnits = requestAllowed.rateUnits();
        final var headers = new HttpHeaders();
        headers.add(
                RATE_LIMIT_REQUESTS_REMAINING_HEADER,
                allowedHeaderValue(rateUnits, requestAllowed.remainingRequests())
        );
        headers.add(
                RATE_LIMIT_REQUESTS_LIMIT_HEADER,
                allowedHeaderValue(rateUnits, requestAllowed.requestsLimit())
        );
        return headers;
    }

    private static String allowedHeaderValue(final Set<RateUnit> rateUnits, final Map<RateUnit, Integer> requests) {
        final var parameterTemplate = "per-%s=%s";
        return rateUnits.stream()
                .map(rateUnit -> parameterTemplate.formatted(rateUnit, requests.get(rateUnit)))
                .collect(Collectors.joining(";"));
    }
}
