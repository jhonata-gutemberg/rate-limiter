package dev.gutemberg.rate.limiter.vendors.s3.contracts;

public interface Logger {
    void error(String message, Throwable exception);
}
