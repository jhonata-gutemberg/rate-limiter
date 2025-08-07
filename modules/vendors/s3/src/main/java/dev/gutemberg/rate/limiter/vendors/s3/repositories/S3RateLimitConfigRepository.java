package dev.gutemberg.rate.limiter.vendors.s3.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities.RateLimitConfig;
import dev.gutemberg.rate.limiter.vendors.domain.rate.limit.contracts.repositories.RateLimitConfigRepository;
import dev.gutemberg.rate.limiter.commons.contracts.loggers.Logger;
import dev.gutemberg.rate.limiter.commons.contracts.loggers.LoggerFactory;
import dev.gutemberg.rate.limiter.vendors.s3.converters.ConfigFromFileToRateLimitConfigConverter;
import dev.gutemberg.rate.limiter.vendors.s3.models.entities.RateLimitConfigFromFile;
import jakarta.inject.Named;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Named
public class S3RateLimitConfigRepository implements RateLimitConfigRepository {
    private final Logger logger;
    private final S3Client s3Client;

    public S3RateLimitConfigRepository(final LoggerFactory loggerFactory, final S3Client s3Client) {
        this.logger = loggerFactory.getLogger(getClass());
        this.s3Client = s3Client;
    }

    @Override
    public Set<RateLimitConfig> findAll() {
        final var objectMapper = new ObjectMapper(new YAMLFactory());
        final var request = GetObjectRequest.builder().bucket("ratelimiter-configs").key("limits.yml").build();
        try (final var response = s3Client.getObject(request)) {
            final var configs = objectMapper.readValue(response.readAllBytes(), RateLimitConfigFromFile[].class);
            return Arrays.stream(configs)
                    .map(ConfigFromFileToRateLimitConfigConverter::convert)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (final IOException exception) {
            logger.error(exception.getMessage(), exception);
            return Set.of();
        }
    }
}
