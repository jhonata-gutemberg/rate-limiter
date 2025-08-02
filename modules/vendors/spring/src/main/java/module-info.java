module dev.gutemberg.rate.limiter.vendors.spring {
    requires dev.gutemberg.rate.limiter.vendors.redis;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires com.fasterxml.jackson.databind;
    requires spring.data.redis;
    requires org.jobrunr.core;
}
