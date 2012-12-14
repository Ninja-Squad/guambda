package com.ninja_squad.guambda;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class FromGuava {
    private FromGuava() {
    }

    public static <T> Predicate<T> toLambda(com.google.common.base.Predicate<? super T> guavaPredicate) {
        return (T t) -> guavaPredicate.apply(t);
    }
    
    public static <T, R> Function<T, R> toLambda(com.google.common.base.Function<? super T, ? extends R> guavaFunction) {
        return (T t) -> guavaFunction.apply(t);
    }
    
    public static <T> Optional<T> toLambda(com.google.common.base.Optional<? extends T> guavaOptional) {
        return guavaOptional.isPresent() ? Optional.of(guavaOptional.get()) : Optional.<T>empty();
    }

    public static <T> Supplier<T> toLambda(com.google.common.base.Supplier<? extends T> guavaSupplier) {
        return () -> guavaSupplier.get();
    }
}