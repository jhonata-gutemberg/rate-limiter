package dev.gutemberg.rate.limiter.commons.contracts.loggers;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);
}
