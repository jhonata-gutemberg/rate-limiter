package dev.gutemberg.rate.limiter.domain.enums;

import org.springframework.http.HttpMethod;

public enum Action {
    CREATE;

    public static Action from(final HttpMethod method) {
        return switch (method.name()) {
            case "POST" -> CREATE;
            default -> throw new RuntimeException();
        };
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
