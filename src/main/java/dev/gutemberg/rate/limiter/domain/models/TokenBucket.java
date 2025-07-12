package dev.gutemberg.rate.limiter.domain.models;

public class TokenBucket {
    private int availableTokens;

    public TokenBucket() {}

    public TokenBucket(final int availableTokens) {
        this.availableTokens = availableTokens;
    }

    public int getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(int availableTokens) {
        this.availableTokens = availableTokens;
    }

    public boolean hasAvailableTokens() {
        return availableTokens > 0;
    }

    public void decrement() {
        availableTokens--;
    }
}
