package dev.gutemberg.rate.limiter.entrypoints.api.controllers;

import dev.gutemberg.rate.limiter.entrypoints.api.converters.HttpMethodAndRequestToApplyRateLimitUseCaseInputConverter;
import dev.gutemberg.rate.limiter.entrypoints.api.builders.HttpHeadersBuilder;
import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.usecases.ApplyRateLimitUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProxyController {
    private final ApplyRateLimitUseCase applyRateLimitUseCase;

    public ProxyController(final ApplyRateLimitUseCase applyRateLimitUseCase) {
        this.applyRateLimitUseCase = applyRateLimitUseCase;
    }

    @RequestMapping(value = "/**", method = {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE})
    public ResponseEntity<Void> proxy(final HttpMethod method, final HttpServletRequest request) {
        final var input = HttpMethodAndRequestToApplyRateLimitUseCaseInputConverter.convert(method, request);
        final var output = applyRateLimitUseCase.perform(input);
        return output.isAllowed() ?
                new ResponseEntity<>(HttpHeadersBuilder.build(output.allowed()), HttpStatus.OK) :
                new ResponseEntity<>(HttpHeadersBuilder.build(output.denied()), HttpStatus.TOO_MANY_REQUESTS);
    }
}
