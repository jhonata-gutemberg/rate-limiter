package dev.gutemberg.rate.limiter.infra.spring.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gutemberg.rate.limiter.infra.redis.contracts.RedisTemplateFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class SpringRedisTemplateFactory implements RedisTemplateFactory {
    private final RedisConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    public SpringRedisTemplateFactory(final RedisConnectionFactory connectionFactory, final ObjectMapper objectMapper) {
        this.connectionFactory = connectionFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public <V> RedisTemplate<String, V> getTemplate(final Class<V> clazz) {
        final RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        final RedisSerializer<V> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, clazz);
        final RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
