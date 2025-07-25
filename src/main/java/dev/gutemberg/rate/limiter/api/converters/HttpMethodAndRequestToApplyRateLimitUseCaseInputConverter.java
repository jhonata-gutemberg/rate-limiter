package dev.gutemberg.rate.limiter.api.converters;

import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput.Action;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.By;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import java.util.Map;

import static dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput.Action.*;

public class HttpMethodAndRequestToApplyRateLimitUseCaseInputConverter {

    private HttpMethodAndRequestToApplyRateLimitUseCaseInputConverter() {}

    public static ApplyRateLimitUseCaseInput convert(final HttpMethod httpMethod, final HttpServletRequest request) {
        final var resource = request.getRequestURI().replace("/api/", "");
        final var identifiers = Map.of(By.IP, request.getRemoteAddr());
        return new ApplyRateLimitUseCaseInput(convert(httpMethod), resource, identifiers);
    }

    private static Action convert(final HttpMethod method) {
        return switch (method.name()) {
            case "POST" -> CREATE;
            case "GET" -> READ;
            case "PUT", "PATCH" -> UPDATE;
            case "DELETE" -> DELETE;
            default -> UNKNOWN;
        };
    }
}
