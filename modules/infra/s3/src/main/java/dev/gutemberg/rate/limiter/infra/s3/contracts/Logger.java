package dev.gutemberg.rate.limiter.infra.s3.contracts;

public interface Logger {
    void error(String message, Throwable exception);
}
