package dev.gutemberg.rate.limiter.infra.spring.adapters;

import dev.gutemberg.rate.limiter.infra.redis.contracts.HashOperations;
import java.util.Map;

public class SpringHashOperationsAdapter<V> implements HashOperations<V> {
    private final org.springframework.data.redis.core.HashOperations<String, String, Object> hashOperations;

    public SpringHashOperationsAdapter(
            final org.springframework.data.redis.core.HashOperations<String, String, Object> hashOperations
    ) {
        this.hashOperations = hashOperations;
    }

    @Override
    public Map<String, Object> entries(final String key) {
        return hashOperations.entries(key);
    }

    @Override
    public void put(final String key, final String hashKey, final V value) {
        hashOperations.put(key, hashKey, value);
    }
}
