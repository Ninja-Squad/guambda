package com.ninja_squad.guambda;

public interface IndexedMapper<R, T> {
    R map(int index, T t);
    
    <V> IndexedMapper<V, T> compose(IndexedMapper<? extends V, ? super R> after) default {
        return IndexedMappers.chain(this, after);
    }
}
