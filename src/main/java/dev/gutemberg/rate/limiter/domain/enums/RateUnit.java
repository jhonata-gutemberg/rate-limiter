package dev.gutemberg.rate.limiter.domain.enums;

public enum RateUnit {
    SECONDS(),
    MINUTES(60),
    HOURS(MINUTES.valueInSeconds() * 60),
    DAYS(HOURS.valueInSeconds() * 24);

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
