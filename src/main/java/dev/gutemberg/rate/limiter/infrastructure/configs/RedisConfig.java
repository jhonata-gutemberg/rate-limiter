package dev.gutemberg.rate.limiter.infrastructure.configs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
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
    public RedisTemplate<String, ?> redisTemplate(final RedisConnectionFactory factory, final ObjectMapper objectMapper) {
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
        );
        final RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        final RedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        final RedisTemplate<String, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
