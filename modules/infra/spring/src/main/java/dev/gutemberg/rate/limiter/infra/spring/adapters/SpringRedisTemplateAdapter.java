package dev.gutemberg.rate.limiter.infra.spring.adapters;

import dev.gutemberg.rate.limiter.infra.redis.contracts.HashOperations;
import dev.gutemberg.rate.limiter.infra.redis.contracts.RedisTemplate;
import dev.gutemberg.rate.limiter.infra.redis.contracts.SetOperations;

public class SpringRedisTemplateAdapter<V> implements RedisTemplate<V> {
    private final org.springframework.data.redis.core.RedisTemplate<String, V> redisTemplate;

    public SpringRedisTemplateAdapter(org.springframework.data.redis.core.RedisTemplate<String, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public SetOperations<V> opsForSet() {
        return new SpringSetOperationsAdapter<>(redisTemplate.opsForSet());
    }

    @Override
    public HashOperations<V> opsForHash() {
        return new SpringHashOperationsAdapter<>(redisTemplate.opsForHash());
    }
}
