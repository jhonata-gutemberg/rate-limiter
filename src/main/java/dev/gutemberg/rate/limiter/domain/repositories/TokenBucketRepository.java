package dev.gutemberg.rate.limiter.domain.repositories;

import dev.gutemberg.rate.limiter.domain.models.TokenBucket;
import dev.gutemberg.rate.limiter.domain.models.TokenBucketCollectionKey;
import java.util.List;

public interface TokenBucketRepository {
    List<TokenBucket> findAllByCollectionKey(TokenBucketCollectionKey collectionKey);
    void put(TokenBucketCollectionKey collectionKey, TokenBucket tokenBucket);
}
