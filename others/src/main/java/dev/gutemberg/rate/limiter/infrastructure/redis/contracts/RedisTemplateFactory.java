package dev.gutemberg.rate.limiter.infrastructure.redis.contracts;

import org.springframework.data.redis.core.RedisTemplate;

public interface RedisTemplateFactory {
    <V> RedisTemplate<String, V> getTemplate(Class<V> clazz);
}
