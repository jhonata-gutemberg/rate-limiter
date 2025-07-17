package dev.gutemberg.rate.limiter.api.controllers;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.ApplyRateLimitUseCaseInput;
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

import static dev.gutemberg.rate.limiter.api.models.RateLimitHttpHeadersBuilder.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProxyController {
    private final Converter<Pair<HttpMethod, HttpServletRequest>, ApplyRateLimitUseCaseInput>
            httpMethodAndRequestToApplyRateLimitUseCaseInputConverter;
    private final ApplyRateLimitUseCase applyRateLimitUseCase;

    public ProxyController(
            final Converter<Pair<HttpMethod, HttpServletRequest>, ApplyRateLimitUseCaseInput>
                    httpMethodAndRequestToApplyRateLimitUseCaseInputConverter,
            final ApplyRateLimitUseCase applyRateLimitUseCase
    ) {
        this.httpMethodAndRequestToApplyRateLimitUseCaseInputConverter =
                httpMethodAndRequestToApplyRateLimitUseCaseInputConverter;
        this.applyRateLimitUseCase = applyRateLimitUseCase;
    }

    @RequestMapping(value = "/**", method = {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE})
    public ResponseEntity<Void> proxy(final HttpMethod method, final HttpServletRequest request) {
        final var input = Objects.requireNonNull(
                httpMethodAndRequestToApplyRateLimitUseCaseInputConverter.convert(Pair.of(method, request))
        );
        final var output = applyRateLimitUseCase.perform(input);
        return output.isAllowed() ?
                new ResponseEntity<>(buildHeaders(output.allowed()), HttpStatus.OK) :
                new ResponseEntity<>(buildHeaders(output.denied()), HttpStatus.TOO_MANY_REQUESTS);
    }
}
