package dev.gutemberg.rate.limiter.vendors.s3.converters;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig;
import dev.gutemberg.rate.limiter.vendors.s3.models.RateLimitConfigFromFile;
import java.util.stream.Collectors;

import static dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig.Limit;
import static dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig.Limit.By;
import static dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig.Limit.Unit;

public class ConfigFromFileToRateLimitConfigConverter {
    private ConfigFromFileToRateLimitConfigConverter() {}

    public static RateLimitConfig convert(final RateLimitConfigFromFile config) {
        final var key = config.action() + ":" + config.resource();
        final var limits = config.limits()
                .stream()
                .map(ConfigFromFileToRateLimitConfigConverter::convert)
                .collect(Collectors.toUnmodifiableSet());
        return new RateLimitConfig(key, limits);
    }

    private static Limit convert(final RateLimitConfigFromFile.Limit limit) {
        final var by = By.valueOf(limit.by().toUpperCase());
        final var unit = Unit.valueOf(limit.unit().toUpperCase());
        return new Limit(by, unit, limit.requests_per_unit());
    }
}
