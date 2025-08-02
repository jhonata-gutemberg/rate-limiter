package dev.gutemberg.rate.limiter.vendors.redis.contracts;

public interface RedisTemplate<V> {
    SetOperations<V> opsForSet();
    HashOperations<V> opsForHash();
}
