package com.ninja_squad.guambda;

import java.util.functions.Block;

public interface IndexedFlatMapper<R, T> {
    void flatMapInto(Block<? super R> sink, int index, T t);
}
