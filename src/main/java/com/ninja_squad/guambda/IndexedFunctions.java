package com.ninja_squad.guambda;

import java.util.Objects;
import java.util.function.Function;

public final class IndexedFunctions {
    private IndexedFunctions() {
    }

    public static <T, R> IndexedFunction<T, R> wrap(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return (i, t) -> mapper.apply(t);
    }
    
    public static <R, T, U> IndexedFunction<T, R> chain(IndexedFunction<? super T, ? extends U> first,
                                                        IndexedFunction<? super U, ? extends R> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        return (i, t) -> second.apply(i, first.apply(i, t));
    }

    public static <R, T> IndexedFunction<T, R> forPredicate(IndexedPredicate<? super T> predicate, R forTrue, R forFalse) {
        Objects.requireNonNull(predicate);

        return (i, t) -> predicate.test(i, t) ? forTrue : forFalse;
    }
}