package me.aj4real.connector;

import java.util.Optional;
import java.util.function.Supplier;

public class Mono<T> {
    Supplier<T> returnValue;
    private Mono(Supplier<T> returnValue) {
         this.returnValue = returnValue;
    }
    public static <T> Mono<T> of(Supplier<T> returnValue) {
        return new Mono(returnValue);
    }
    public T block() {
        return returnValue.get();
    }
}
