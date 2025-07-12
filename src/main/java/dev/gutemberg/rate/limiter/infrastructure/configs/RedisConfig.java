package dev.gutemberg.rate.limiter.infrastructure.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.domain.models.RateLimitCollectionKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<RateLimitCollectionKey, List<RateLimit>> redisTemplate(
            final RedisConnectionFactory factory,
            final ObjectMapper objectMapper
    ) {
        final var stringSerializer = new StringRedisSerializer();
        final var jacksonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, RateLimit.class);
        return redisTemplate(factory, stringSerializer, jacksonSerializer);
    }

    private <K, V> RedisTemplate<RateLimitCollectionKey, List<RateLimit>> redisTemplate(
            final RedisConnectionFactory factory,
            final RedisSerializer<K> keySerializer,
            final RedisSerializer<V> valueSerializer
    ) {
        final RedisTemplate<RateLimitCollectionKey, List<RateLimit>> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer);
        template.setHashValueSerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
