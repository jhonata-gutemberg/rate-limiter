package dev.gutemberg.rate.limiter.vendors.slf4j.factories;

import dev.gutemberg.rate.limiter.vendors.slf4j.adapters.SLF4JLogger;
import dev.gutemberg.rate.limiter.commons.contracts.loggers.Logger;
import dev.gutemberg.rate.limiter.commons.contracts.loggers.LoggerFactory;
import jakarta.inject.Named;

@Named
public class SLF4JLoggerFactory implements LoggerFactory {
    @Override
    public Logger getLogger(Class<?> clazz) {
        return new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(clazz));
    }
}
