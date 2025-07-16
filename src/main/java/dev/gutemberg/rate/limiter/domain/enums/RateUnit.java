package dev.gutemberg.rate.limiter.domain.enums;

public enum RateUnit {
    SECOND(),
    MINUTE(60),
    HOUR(MINUTE.valueInSeconds() * 60),
    DAY(HOUR.valueInSeconds() * 24);

    private final int valueInSeconds;

    RateUnit() {
        valueInSeconds = 1;
    }

    RateUnit(final int valueInSeconds) {
        this.valueInSeconds = valueInSeconds;
    }

    public int valueInSeconds() {
        return valueInSeconds;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
