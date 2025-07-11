package dev.gutemberg.rate.limiter.domain.models;

import dev.gutemberg.rate.limiter.domain.enums.RateUnit;

public class TokenBucket {
    private final RateUnit refillRate;
    private int availableTokens;

    public TokenBucket(final RateUnit refillRate, final int availableTokens) {
        this.refillRate = refillRate;
        this.availableTokens = availableTokens;
    }

    public RateUnit refillRate() {
        return refillRate;
    }

    public boolean hasAvailableTokens() {
        return availableTokens > 0;
    }

    public void decrement() {
        availableTokens--;
    }
}
