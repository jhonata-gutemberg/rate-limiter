package dev.gutemberg.rate.limiter.api.converters;

import dev.gutemberg.rate.limiter.api.contracts.Converter;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput.Action;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.By;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import java.util.Map;

import static dev.gutemberg.rate.limiter.domain.rate.limit.contracts.usecases.ApplyRateLimitUseCaseInput.Action.*;

@Component
public class HttpMethodAndRequestToApplyRateLimitUseCaseInputConverter
        implements Converter<Pair<HttpMethod, HttpServletRequest>, ApplyRateLimitUseCaseInput> {

    @Override
    public ApplyRateLimitUseCaseInput convert(Pair<HttpMethod, HttpServletRequest> pair) {
        final var request = pair.getSecond();
        final var resource = request.getRequestURI().replace("/api/", "");
        final var identifiers = Map.of(By.IP, request.getRemoteAddr());
        return new ApplyRateLimitUseCaseInput(convert(pair.getFirst()), resource, identifiers);
    }

    private Action convert(final HttpMethod method) {
        return switch (method.name()) {
            case "POST" -> CREATE;
            case "GET" -> READ;
            case "PUT", "PATCH" -> UPDATE;
            case "DELETE" -> DELETE;
            default -> UNKNOWN;
        };
    }
}
