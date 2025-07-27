package dev.gutemberg.rate.limiter.entrypoints.api.models;

import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseOutput.Allowed;
import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseOutput.*;

public class HttpHeadersBuilder {
    private static final String RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER = "X-RateLimit-Retry-After-Seconds";
    private static final String RATE_LIMIT_REQUESTS_REMAINING_HEADER = "X-RateLimit-Requests-Remaining";
    private static final String RATE_LIMIT_REQUESTS_LIMIT_HEADER = "X-RateLimit-Requests-Limit";

    private HttpHeadersBuilder() {}

    public static HttpHeaders build(final Allowed allowed) {
        final var data = allowed.data();
        final var headers = new HttpHeaders();
        if (data.isEmpty()) {
            return headers;
        }
        headers.add(RATE_LIMIT_REQUESTS_REMAINING_HEADER, toString(data, Allowed.Info::requestsAvailable));
        headers.add(RATE_LIMIT_REQUESTS_LIMIT_HEADER, toString(data, Allowed.Info::requestsPerUnit));
        return headers;
    }

    public static HttpHeaders build(final Denied denied) {
        final var retryAfterInSeconds = Duration.between(Instant.now(), denied.nextRefillAt()).getSeconds();
        final var headers = new HttpHeaders();
        headers.add(RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER, String.valueOf(retryAfterInSeconds));
        return headers;
    }

    private static String toString(Set<Allowed.Info> data, final ToIntFunction<Allowed.Info> requestsExtractor) {
        return data.stream()
                .map(info -> toString(info, requestsExtractor))
                .collect(Collectors.joining(";"));
    }

    private static String toString(final Allowed.Info info, final ToIntFunction<Allowed.Info> requestsExtractor) {
        return "per-%s=%s".formatted(info.unit().name().toLowerCase(), requestsExtractor.applyAsInt(info));
    }
}
