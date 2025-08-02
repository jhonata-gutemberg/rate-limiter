package dev.gutemberg.rate.limiter.infra.s3.contracts;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);
}
