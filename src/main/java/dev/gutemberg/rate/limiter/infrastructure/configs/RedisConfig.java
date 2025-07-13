package dev.gutemberg.rate.limiter.infrastructure.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gutemberg.rate.limiter.domain.models.RateLimit;
import dev.gutemberg.rate.limiter.infrastructure.models.TokenBucketValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, RateLimit> rateLimitRedisTemplate(
            final RedisConnectionFactory factory,
            final ObjectMapper objectMapper
    ) {
        return redisTemplate(factory, objectMapper, RateLimit.class);
    }

    @Bean
    public RedisTemplate<String, TokenBucketValue> tokenBucketRedisTemplate(
            final RedisConnectionFactory factory,
            final ObjectMapper objectMapper
    ) {
        return redisTemplate(factory, objectMapper, TokenBucketValue.class);
    }

    private <V> RedisTemplate<String, V> redisTemplate(
            final RedisConnectionFactory factory,
            final ObjectMapper objectMapper,
            final Class<V> value
    ) {
        final RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        final RedisSerializer<V> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, value);
        final RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
