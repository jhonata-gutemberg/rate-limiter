package dev.gutemberg.rate.limiter.infra.redis.contracts;

public interface RedisTemplateFactory {
    <V> RedisTemplate<V> getTemplate(Class<V> clazz);
}
