package dev.gutemberg.rate.limiter.vendors.redis.contracts;

public interface RedisTemplateFactory {
    <V> RedisTemplate<V> getTemplate(Class<V> clazz);
}
