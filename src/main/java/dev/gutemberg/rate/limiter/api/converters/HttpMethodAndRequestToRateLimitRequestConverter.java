package dev.gutemberg.rate.limiter.api.converters;

import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.models.RateLimitRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class HttpMethodAndRequestToRateLimitRequestConverter
        implements Converter<Pair<HttpMethod, HttpServletRequest>, RateLimitRequest> {
    private final Converter<HttpMethod, RateLimitRequestAction> httpMethodToRateLimitRequestActionConverter;

    public HttpMethodAndRequestToRateLimitRequestConverter(
            final Converter<HttpMethod, RateLimitRequestAction> httpMethodToRateLimitRequestActionConverter
    ) {
        this.httpMethodToRateLimitRequestActionConverter = httpMethodToRateLimitRequestActionConverter;
    }

    @Override
    public RateLimitRequest convert(Pair<HttpMethod, HttpServletRequest> pair) {
        final var action = httpMethodToRateLimitRequestActionConverter.convert(pair.getFirst());
        final var request = pair.getSecond();
        final var resource = request.getRequestURI().replace("/api/", "");
        final var identifiers = Map.of(RateLimitConfig.Limit.By.IP, request.getRemoteAddr());
        return new RateLimitRequest(action, resource, identifiers);
    }
}
