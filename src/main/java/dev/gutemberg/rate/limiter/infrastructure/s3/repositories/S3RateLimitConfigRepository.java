package dev.gutemberg.rate.limiter.infrastructure.s3.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.rate.limit.repositories.RateLimitConfigRepository;
import dev.gutemberg.rate.limiter.infrastructure.commons.contracts.Converter;
import dev.gutemberg.rate.limiter.infrastructure.s3.models.RateLimitConfigFromFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class S3RateLimitConfigRepository implements RateLimitConfigRepository {
    private final ResourceLoader resourceLoader;
    private final Converter<RateLimitConfigFromFile, RateLimitConfig> configFromFileToRateLimitConfigConverter;
    private final Logger logger;

    public S3RateLimitConfigRepository(
            final ResourceLoader resourceLoader,
            final Converter<RateLimitConfigFromFile, RateLimitConfig> configFromFileToRateLimitConfigConverter
    ) {
        this.resourceLoader = resourceLoader;
        this.configFromFileToRateLimitConfigConverter = configFromFileToRateLimitConfigConverter;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Set<RateLimitConfig> findAll() {
        try {
            final var inputStream = resourceLoader.getResource("classpath:limits.yml").getInputStream();
            final var objectMapper = new ObjectMapper(new YAMLFactory());
            return Arrays.stream(objectMapper.readValue(inputStream, RateLimitConfigFromFile[].class))
                    .map(configFromFileToRateLimitConfigConverter::convert)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (final IOException exception) {
            logger.error(exception.getMessage(), exception);
            return Set.of();
        }
    }
}
