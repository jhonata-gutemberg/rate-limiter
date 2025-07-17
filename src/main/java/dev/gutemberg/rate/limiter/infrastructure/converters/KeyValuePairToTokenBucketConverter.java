package dev.gutemberg.rate.limiter.infrastructure.converters;

import dev.gutemberg.rate.limiter.domain.token.bucket.models.TokenBucket;
import dev.gutemberg.rate.limiter.infrastructure.models.TokenBucketValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class KeyValuePairToTokenBucketConverter implements Converter<Pair<String, TokenBucketValue>, TokenBucket> {
    @Override
    public TokenBucket convert(final Pair<String, TokenBucketValue> pair) {
        return new TokenBucket(pair.getFirst(), pair.getSecond().availableTokens());
    }
}
