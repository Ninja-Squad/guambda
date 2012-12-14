package com.ninja_squad.guambda;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

public final class FromLambda {
    private FromLambda() {
    }
    
    public static <T> Predicate<T> toGuava(java.util.function.Predicate<? super T> lambdaPredicate) {
        return (T t) -> lambdaPredicate.test(t);
    }
    
    public static <T, R> Function<T, R> toGuava(java.util.function.Function<? super T, ? extends R> lambdaFunction) {
        return (T t) -> lambdaFunction.apply(t);
    }
    
    public static <T> Optional<T> toGuava(java.util.Optional<? extends T> lambdaOptional) {
        T nullable = lambdaOptional.isPresent() ? lambdaOptional.get() : null;
        return Optional.fromNullable(nullable);
    }

    public static <T> Supplier<T> toGuava(java.util.function.Supplier<? extends T> lambdaSupplier) {
        return () -> lambdaSupplier.get();
    }
}