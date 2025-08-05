package dev.gutemberg.rate.limiter.vendors.s3.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.entities.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories.RateLimitConfigRepository;
import dev.gutemberg.rate.limiter.commons.contracts.loggers.Logger;
import dev.gutemberg.rate.limiter.commons.contracts.loggers.LoggerFactory;
import dev.gutemberg.rate.limiter.vendors.s3.converters.ConfigFromFileToRateLimitConfigConverter;
import dev.gutemberg.rate.limiter.vendors.s3.models.RateLimitConfigFromFile;
import jakarta.inject.Named;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Named
public class S3RateLimitConfigRepository implements RateLimitConfigRepository {
    private final ResourceLoader resourceLoader;
    private final Logger logger;

    public S3RateLimitConfigRepository(final ResourceLoader resourceLoader, final LoggerFactory loggerFactory) {
        this.resourceLoader = resourceLoader;
        this.logger = loggerFactory.getLogger(getClass());
    }

    @Override
    public Set<RateLimitConfig> findAll() {
        try {
            final var inputStream = resourceLoader.getResource("classpath:limits.yml").getInputStream();
            final var objectMapper = new ObjectMapper(new YAMLFactory());
            return Arrays.stream(objectMapper.readValue(inputStream, RateLimitConfigFromFile[].class))
                    .map(ConfigFromFileToRateLimitConfigConverter::convert)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (final IOException exception) {
            logger.error(exception.getMessage(), exception);
            return Set.of();
        }
    }
}
