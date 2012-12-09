package com.ninja_squad.guambda;

import java.util.Optional;
import java.util.functions.Mapper;
import java.util.functions.Predicate;

public final class FromGuava {
    private FromGuava() {
    }

    public static <T> Predicate<T> toLambda(com.google.common.base.Predicate<? super T> guavaPredicate) {
        return (T t) -> guavaPredicate.apply(t);
    }
    
    public static <R, T> Mapper<R, T> toLambda(com.google.common.base.Function<? super T, ? extends R> guavaFunction) {
        return (T t) -> guavaFunction.apply(t);
    }
    
    public static <T> Optional<T> toLambda(com.google.common.base.Optional<? extends T> guavaOptional) {
        return guavaOptional.isPresent() ? new Optional<T>(guavaOptional.get()) : Optional.<T>empty();
    }
}