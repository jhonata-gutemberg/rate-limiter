package dev.gutemberg.rate.limiter.api.controllers;

import dev.gutemberg.rate.limiter.domain.enums.Action;
import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProxyController {
    private final RateLimitRepository rateLimitRepository;

    public ProxyController(final RateLimitRepository rateLimitRepository) {
        this.rateLimitRepository = rateLimitRepository;
    }

    @RequestMapping(value = "/**", method = {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE})
    public void proxy(final HttpMethod method, final HttpServletRequest request) {
        final var action = Action.from(method);
        final var resource = request.getRequestURI().replace("/api/", "");
        rateLimitRepository.findByActionAndResource(action, resource)
                .ifPresent(rateLimit -> {
                    if (rateLimit.limitedBy() == LimitedBy.IP) {
                        limitByIp();
                    }
                });
        //redirect
    }

    public void limitByIp() {
        System.out.println("limiting by ip...");
    }
}
