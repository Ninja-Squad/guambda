package com.ninja_squad.guambda;

public interface IndexedBlock<T> {
    void accept(int index, T t);
}
