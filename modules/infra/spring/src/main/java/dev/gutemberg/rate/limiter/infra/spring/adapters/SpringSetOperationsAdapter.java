package dev.gutemberg.rate.limiter.infra.spring.adapters;

import dev.gutemberg.rate.limiter.infra.redis.contracts.SetOperations;

import java.util.Set;

public class SpringSetOperationsAdapter<V> implements SetOperations<V> {
    private final org.springframework.data.redis.core.SetOperations<String, V> setOperations;

    public SpringSetOperationsAdapter(
            final org.springframework.data.redis.core.SetOperations<String, V> setOperations
    ) {
        this.setOperations = setOperations;
    }

    @Override
    public Set<V> members(final String key) {
        return setOperations.members(key);
    }

    @Override
    public void add(final String key, final V value) {
        setOperations.add(key, value);
    }
}
