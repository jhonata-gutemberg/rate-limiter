package dev.gutemberg.rate.limiter.domain.token.bucket.models;

import java.time.Instant;

public class TokenBucket {
    private final String identifier;
    private int availableTokens;
    private final Instant nextRefillAt;

    public TokenBucket(final String identifier, final int availableTokens, final Instant nextRefillAt) {
        this.identifier = identifier;
        this.availableTokens = availableTokens;
        this.nextRefillAt = nextRefillAt;
    }

    public String identifier() {
        return identifier;
    }

    public int availableTokens() {
        return availableTokens;
    }

    public boolean hasAvailableTokens() {
        return availableTokens > 0;
    }

    public void consumeToken() {
        availableTokens--;
    }

    public Instant nextRefillAt() {
        return nextRefillAt;
    }
}
