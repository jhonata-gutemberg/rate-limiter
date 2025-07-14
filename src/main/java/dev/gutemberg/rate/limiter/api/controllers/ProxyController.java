package dev.gutemberg.rate.limiter.api.controllers;

import dev.gutemberg.rate.limiter.api.models.RateLimitHttpHeadersBuilder;
import dev.gutemberg.rate.limiter.domain.models.RateLimitRequest;
import dev.gutemberg.rate.limiter.domain.usecases.ApplyRateLimitUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProxyController {
    private final Converter<Pair<HttpMethod, HttpServletRequest>, RateLimitRequest>
            httpMethodAndRequestToRateLimitRequestConverter;
    private final ApplyRateLimitUseCase applyRateLimitUseCase;

    public ProxyController(
            final Converter<Pair<HttpMethod, HttpServletRequest>, RateLimitRequest>
                    httpMethodAndRequestToRateLimitRequestConverter,
            final ApplyRateLimitUseCase applyRateLimitUseCase
    ) {
        this.httpMethodAndRequestToRateLimitRequestConverter = httpMethodAndRequestToRateLimitRequestConverter;
        this.applyRateLimitUseCase = applyRateLimitUseCase;
    }

    @RequestMapping(value = "/**", method = {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE})
    public ResponseEntity<Void> proxy(final HttpMethod method, final HttpServletRequest request) {
        final var rateLimitRequest = Objects.requireNonNull(
                httpMethodAndRequestToRateLimitRequestConverter.convert(Pair.of(method, request))
        );
        final var rateLimitResponse = applyRateLimitUseCase.perform(rateLimitRequest);
        if (rateLimitResponse.hasRequestDenied()) {
            return new ResponseEntity<>(
                    RateLimitHttpHeadersBuilder.denied(rateLimitResponse.requestDenied()),
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }
        return new ResponseEntity<>(
                RateLimitHttpHeadersBuilder.allowed(rateLimitResponse.requestAllowed()),
                HttpStatus.OK
        );
    }
}
