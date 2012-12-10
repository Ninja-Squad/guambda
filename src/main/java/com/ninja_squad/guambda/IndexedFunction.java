package com.ninja_squad.guambda;

public interface IndexedFunction<T, R> {
    R apply(int index, T t);
    
    default <V> IndexedFunction<T, V> compose(IndexedFunction<? super R, ? extends V> after) {
        return IndexedFunctions.chain(this, after);
    }
}
