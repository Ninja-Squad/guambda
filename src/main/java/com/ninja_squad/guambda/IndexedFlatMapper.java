package com.ninja_squad.guambda;

import java.util.function.Block;

public interface IndexedFlatMapper<T, R> {
    void flatMapInto(Block<? super R> sink, int index, T t);
}
