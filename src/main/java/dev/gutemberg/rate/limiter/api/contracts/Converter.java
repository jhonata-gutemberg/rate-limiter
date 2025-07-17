package dev.gutemberg.rate.limiter.api.contracts;

public interface Converter<I, O> {
    O convert(I input);
}
