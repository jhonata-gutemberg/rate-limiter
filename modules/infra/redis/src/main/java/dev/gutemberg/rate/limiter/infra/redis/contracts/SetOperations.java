package dev.gutemberg.rate.limiter.infra.redis.contracts;

import java.util.Set;

public interface SetOperations<V> {
    Set<V> members(String key);
    void add(String key, V value);
}
