package dev.gutemberg.rate.limiter.infrastructure.converters;

import dev.gutemberg.rate.limiter.domain.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.models.TokenBucketKey;
import dev.gutemberg.rate.limiter.infrastructure.models.TokenBucketValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class KeyValuePairToTokenBucketConverter implements Converter<Pair<TokenBucketKey, TokenBucketValue>, TokenBucket> {
    @Override
    public TokenBucket convert(final Pair<TokenBucketKey, TokenBucketValue> keyValuePair) {
        return new TokenBucket(keyValuePair.getFirst(), keyValuePair.getSecond().availableTokens());
    }
}
