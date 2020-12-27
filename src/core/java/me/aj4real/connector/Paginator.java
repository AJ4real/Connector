package me.aj4real.connector;

import java.util.function.Function;

public class Paginator<T> {
    private final Function<Integer,T> returnValue;
    private int page = 0;
    private Paginator(Function<Integer, T> returnValue) {
        this.returnValue = returnValue;
    }
    public static <T> Paginator<T> of(Function<Integer, T> returnValue) {
        return new Paginator(returnValue);
    }
    public T next() {
        page++;
        return returnValue.apply(page);
    }

}
