package me.aj4real.connector;

import java.util.function.Supplier;

public class Task<T> {
    private Supplier<T> returnValue;
    private Task(Supplier<T> returnValue) {
         this.returnValue = returnValue;
    }
    public static <T> Task<T> of(Supplier<T> returnValue) {
        return new Task(returnValue);
    }
    public T block() {
        return returnValue.get();
    }
}
