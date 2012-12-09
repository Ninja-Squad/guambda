package com.ninja_squad.guambda;

import java.util.Objects;
import java.util.functions.Mapper;

public final class IndexedMappers {
    private IndexedMappers() {
    }

    public static <R, T> IndexedMapper<R, T> wrap(Mapper<R, T> mapper) {
        Objects.requireNonNull(mapper);
        return (i, t) -> mapper.map(t);
    }
    
    public static <R, T, U> IndexedMapper<R, T> chain(IndexedMapper<? extends U, ? super T> first,
                                                      IndexedMapper<? extends R, ? super U> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        return (i, t) -> second.map(i, first.map(i, t));
    }

    public static <R, T> IndexedMapper<R, T> forPredicate(IndexedPredicate<? super T> predicate, R forTrue, R forFalse) {
        Objects.requireNonNull(predicate);

        return (i, t) -> predicate.test(i, t) ? forTrue : forFalse;
    }
}