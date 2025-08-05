package dev.gutemberg.rate.limiter.adapters;

import dev.gutemberg.rate.limiter.commons.contracts.loggers.Logger;

public class SLF4JLogger implements Logger {
    private final org.slf4j.Logger logger;

    public SLF4JLogger(final org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void error(String message, Throwable exception) {
        logger.error(message, exception);
    }
}
