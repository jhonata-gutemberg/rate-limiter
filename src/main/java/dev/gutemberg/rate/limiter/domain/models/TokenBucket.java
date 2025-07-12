package dev.gutemberg.rate.limiter.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TokenBucket {
    @JsonIgnore
    private TokenBucketKey key;
    private int availableTokens;

    public TokenBucket() {}

    public TokenBucket(final TokenBucketKey key, final int availableTokens) {
        this.key = key;
        this.availableTokens = availableTokens;
    }

    public int getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(int availableTokens) {
        this.availableTokens = availableTokens;
    }

    public TokenBucketKey getKey() {
        return key;
    }

    public TokenBucket setKey(TokenBucketKey key) {
        this.key = key;
        return this;
    }

    public boolean hasAvailableTokens() {
        return availableTokens > 0;
    }

    public void decrement() {
        availableTokens--;
    }
}
