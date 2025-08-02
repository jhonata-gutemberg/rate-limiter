package dev.gutemberg.rate.limiter.vendors.s3.contracts;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);
}
