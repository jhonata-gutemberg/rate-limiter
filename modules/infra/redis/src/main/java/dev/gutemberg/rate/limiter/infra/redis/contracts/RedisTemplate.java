package dev.gutemberg.rate.limiter.infra.redis.contracts;

public interface RedisTemplate<V> {
    SetOperations<V> opsForSet();
    HashOperations<V> opsForHash();
}
