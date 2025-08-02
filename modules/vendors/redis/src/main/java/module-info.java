module dev.gutemberg.rate.limiter.vendors.redis {
    requires dev.gutemberg.rate.limiter.domain;
    requires jakarta.inject;

    exports dev.gutemberg.rate.limiter.vendors.redis.contracts;
}
