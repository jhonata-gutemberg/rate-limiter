package dev.gutemberg.rate.limiter.api.converters;

import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction.*;

@Component
public class HttpMethodToRateLimitRequestActionConverter implements Converter<HttpMethod, RateLimitRequestAction> {
    @Override
    public RateLimitRequestAction convert(final HttpMethod method) {
        return switch (method.name()) {
            case "POST" -> CREATE;
            case "GET" -> READ;
            case "PUT", "PATCH" -> UPDATE;
            case "DELETE" -> DELETE;
            default -> UNKNOWN;
        };
    }
}
