module redis {
    requires domain;
    requires jakarta.inject;

    exports dev.gutemberg.rate.limiter.vendors.redis.contracts;
}
