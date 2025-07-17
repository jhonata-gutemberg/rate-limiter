package dev.gutemberg.rate.limiter.domain.token.bucket.models;

import dev.gutemberg.rate.limiter.domain.rate.limit.models.RateLimitConfig.Limit.Unit;

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

    public static class KeyBuilder {
        private KeyBuilder() {}

        public static String build(final String configKey, final String limitIdentifier, final Unit rateUnit) {
            return configKey + ":" + limitIdentifier + ":" + rateUnit;
        }
    }
}
