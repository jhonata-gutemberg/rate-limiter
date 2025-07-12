package dev.gutemberg.rate.limiter.domain.models;

public class TokenBucket {
    private final TokenBucketKey key;
    private int availableTokens;

    public TokenBucket(final TokenBucketKey key, final int availableTokens) {
        this.key = key;
        this.availableTokens = availableTokens;
    }

    public TokenBucketKey key() {
        return key;
    }

    public int availableTokens() {
        return availableTokens;
    }

    public boolean hasAvailableTokens() {
        return availableTokens > 0;
    }

    public void decrement() {
        availableTokens--;
    }
}
