package dev.gutemberg.rate.limiter.infrastructure.contracts;

public interface Converter<I, O> {
    O convert(I input);
}
