module domain {
    requires jakarta.inject;

    exports dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.entities;
    exports dev.gutemberg.rate.limiter.vendors.domain.rate.limit.models.usecases;
    exports dev.gutemberg.rate.limiter.vendors.domain.rate.limit.usecases;
    exports dev.gutemberg.rate.limiter.vendors.domain.rate.limit.contracts.schedulers;
    exports dev.gutemberg.rate.limiter.vendors.domain.rate.limit.contracts.repositories;
    exports dev.gutemberg.rate.limiter.vendors.domain.token.bucket.usecases;
    exports dev.gutemberg.rate.limiter.vendors.domain.token.bucket.contracts.repositories;
    exports dev.gutemberg.rate.limiter.vendors.domain.token.bucket.models.entities;
}
