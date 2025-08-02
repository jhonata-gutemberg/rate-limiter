module dev.gutemberg.rate.limiter.domain {
    requires jakarta.inject;

    exports dev.gutemberg.rate.limiter.domain.rate.limit.models.entities;
    exports dev.gutemberg.rate.limiter.domain.rate.limit.models.usecases;
    exports dev.gutemberg.rate.limiter.domain.rate.limit.usecases;
    exports dev.gutemberg.rate.limiter.domain.rate.limit.contracts.schedulers;
    exports dev.gutemberg.rate.limiter.domain.rate.limit.contracts.repositories;
    exports dev.gutemberg.rate.limiter.domain.token.bucket.models;
    exports dev.gutemberg.rate.limiter.domain.token.bucket.usecases;
    exports dev.gutemberg.rate.limiter.domain.token.bucket.contracts.repositories;
}
