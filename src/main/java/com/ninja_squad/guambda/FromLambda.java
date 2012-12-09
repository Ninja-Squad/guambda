package com.ninja_squad.guambda;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public final class FromLambda{
    private FromLambda() {
    }
    
    public static <T> Predicate<T> toGuava(java.util.functions.Predicate<? super T> lambdaPredicate) {
        return (T t) -> lambdaPredicate.test(t);
    }
    
    public static <R, T> Function<T, R> toGuava(java.util.functions.Mapper<? extends R, ? super T> lambdaMapper) {
        return (T t) -> lambdaMapper.map(t);
    }
    
    public static <T> Optional<T> toGuava(java.util.Optional<? extends T> lambdaOptional) {
        return Optional.fromNullable(lambdaOptional.orElse(null));
    }
}