package dev.gutemberg.rate.limiter.infrastructure.repositories.s3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.gutemberg.rate.limiter.domain.models.RateLimitConfig;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitConfigRepository;
import dev.gutemberg.rate.limiter.infrastructure.contracts.Converter;
import dev.gutemberg.rate.limiter.infrastructure.models.RateLimitConfigFromFile;
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
    private final Converter<RateLimitConfigFromFile, RateLimitConfig> fileToRateLimitConfigConverter;
    private final Logger logger;

    public S3RateLimitConfigRepository(
            final ResourceLoader resourceLoader,
            final Converter<RateLimitConfigFromFile, RateLimitConfig> fileToRateLimitConfigConverter
    ) {
        this.resourceLoader = resourceLoader;
        this.fileToRateLimitConfigConverter = fileToRateLimitConfigConverter;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Set<RateLimitConfig> findAll() {
        try {
            final var inputStream = resourceLoader.getResource("classpath:limits.yml").getInputStream();
            final var objectMapper = new ObjectMapper(new YAMLFactory());
            return Arrays.stream(objectMapper.readValue(inputStream, RateLimitConfigFromFile[].class))
                    .map(fileToRateLimitConfigConverter::convert)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (final IOException exception) {
            logger.error(exception.getMessage(), exception);
            return Set.of();
        }
    }
}
