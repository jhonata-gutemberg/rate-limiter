package dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities;

import java.time.Instant;

public class TokenBucket {
    private final String identifier;
    private int availableTokens;
    private Instant nextRefillAt;

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

    public void refill(final TokenBucketRefill refill) {
        availableTokens = refill.refillRate();
        nextRefillAt = Instant.now().plus(1, refill.unit().temporal());
    }

    public void consumeToken() {
        availableTokens--;
    }

    public Instant nextRefillAt() {
        return nextRefillAt;
    }
}
