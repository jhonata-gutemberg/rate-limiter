package dev.gutemberg.rate.limiter.commons.contracts.loggers;

public interface Logger {
    void error(String message, Throwable exception);
}
