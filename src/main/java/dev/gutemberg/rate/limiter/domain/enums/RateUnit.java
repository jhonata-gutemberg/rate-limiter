package dev.gutemberg.rate.limiter.domain.enums;

public enum RateUnit {
    SECONDS,
    MINUTES,
    HOURS,
    DAYS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
