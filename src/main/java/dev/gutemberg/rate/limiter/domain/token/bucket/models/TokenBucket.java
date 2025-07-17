package dev.gutemberg.rate.limiter.domain.token.bucket.models;

public class TokenBucket {
    private final String key;
    private int availableTokens;

    public TokenBucket(final String key, final int availableTokens) {
        this.key = key;
        this.availableTokens = availableTokens;
    }

    public String key() {
        return key;
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
}
