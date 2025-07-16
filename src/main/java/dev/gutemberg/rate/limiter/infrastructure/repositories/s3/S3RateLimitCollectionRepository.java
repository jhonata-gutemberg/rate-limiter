package dev.gutemberg.rate.limiter.infrastructure.repositories.s3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.gutemberg.rate.limiter.domain.enums.LimitedBy;
import dev.gutemberg.rate.limiter.domain.enums.RateLimitRequestAction;
import dev.gutemberg.rate.limiter.domain.enums.RateUnit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollection;
import dev.gutemberg.rate.limiter.domain.repositories.RateLimitCollectionRepository;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class S3RateLimitCollectionRepository implements RateLimitCollectionRepository {
    private final ResourceLoader resourceLoader;

    public S3RateLimitCollectionRepository(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Set<RateLimitCollection> findAll() {
        try {
            final var inputStream = resourceLoader.getResource("classpath:limits.yml").getInputStream();
            final var objectMapper = new ObjectMapper(new YAMLFactory());
            return Arrays.stream(objectMapper.readValue(inputStream, RateLimitConfig[].class))
                    .map(rateLimitConfig ->
                            new RateLimitCollection(
                                    new RateLimitCollection.Key(
                                            RateLimitRequestAction.valueOf(rateLimitConfig.action.toUpperCase()),
                                            rateLimitConfig.resource),
                                    rateLimitConfig.limits.stream()
                                            .map(limit -> new RateLimitCollection.Value(
                                                    LimitedBy.valueOf(limit.by.toUpperCase()),
                                                    RateUnit.valueOf(limit.unit.toUpperCase()),
                                                    limit.requestsPerUnit))
                                            .collect(Collectors.toUnmodifiableSet())
                            ))
                    .collect(Collectors.toUnmodifiableSet());
        } catch (final IOException exception) {
            return Set.of();
        }
    }

    record RateLimitConfig(String action, String resource, Set<Limit> limits) {
        record Limit(String by, String unit, int requestsPerUnit) {}
    }
}
