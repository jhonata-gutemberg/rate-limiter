package dev.gutemberg.rate.limiter.factories;

import dev.gutemberg.rate.limiter.adapters.SLF4JLogger;
import dev.gutemberg.rate.limiter.infra.s3.contracts.Logger;
import dev.gutemberg.rate.limiter.infra.s3.contracts.LoggerFactory;
import jakarta.inject.Named;

@Named
public class SLF4JLoggerFactory implements LoggerFactory {
    @Override
    public Logger getLogger(Class<?> clazz) {
        return new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(clazz));
    }
}
