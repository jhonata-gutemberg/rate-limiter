package dev.gutemberg.rate.limiter.infrastructure.converters;

import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.infrastructure.models.RateLimitConfigFromFile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

import static dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit;
import static dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit.By;
import static dev.gutemberg.rate.limiter.domain.models.RateLimitConfig.Limit.Unit;

@Component
public class ConfigFromFileToRateLimitConfigConverter implements Converter<RateLimitConfigFromFile, RateLimitConfig> {
    @Override
    public RateLimitConfig convert(final RateLimitConfigFromFile config) {
        final var key = config.action() + ":" + config.resource();
        final var limits = config.limits()
                .stream()
                .map(this::convert)
                .collect(Collectors.toUnmodifiableSet());
        return new RateLimitConfig(key, limits);
    }

    private Limit convert(final RateLimitConfigFromFile.Limit limit) {
        final var by = By.valueOf(limit.by().toUpperCase());
        final var unit = Unit.valueOf(limit.unit().toUpperCase());
        return new Limit(by, unit, limit.requests_per_unit());
    }
}
