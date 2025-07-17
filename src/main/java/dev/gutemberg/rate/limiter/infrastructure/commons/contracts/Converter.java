package dev.gutemberg.rate.limiter.infrastructure.commons.contracts;

public interface Converter<I, O> {
    O convert(I input);
}
