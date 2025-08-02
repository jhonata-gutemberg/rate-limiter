module dev.gutemberg.rate.limiter.vendors.s3 {
    requires dev.gutemberg.rate.limiter.domain;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires jakarta.inject;
    requires spring.core;
    exports dev.gutemberg.rate.limiter.vendors.s3.contracts;
}
