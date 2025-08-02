package dev.gutemberg.rate.limiter.vendors.redis.contracts;

import java.util.Map;

public interface HashOperations<V> {
    Map<String, Object> entries(String key);
    void put(String key, String hashKey, V value);
}
