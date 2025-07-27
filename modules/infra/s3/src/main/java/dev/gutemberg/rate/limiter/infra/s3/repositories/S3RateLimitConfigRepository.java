package dev.gutemberg.rate.limiter.infra.s3.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories.RateLimitConfigRepository;
import dev.gutemberg.rate.limiter.infra.s3.converters.ConfigFromFileToRateLimitConfigConverter;
import dev.gutemberg.rate.limiter.infra.s3.models.RateLimitConfigFromFile;
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
    private final Logger logger;

    public S3RateLimitConfigRepository(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.logger = LoggerFactory.getLogger(getClass());
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
