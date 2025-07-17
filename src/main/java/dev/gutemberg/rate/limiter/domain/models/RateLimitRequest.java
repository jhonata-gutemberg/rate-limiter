package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;

import java.util.Map;

public record RateLimitRequest(RateLimitRequestAction action, String resource, Map<RateLimitConfig.Limit.By, String> identifiers) {
}
