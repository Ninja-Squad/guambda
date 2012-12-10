package com.ninja_squad.guambda;

import java.util.*;
import java.util.functions.*;
import com.google.common.collect.*;

public final class Sort<T> implements Comparator<T> {
    private Mapper<?, T> mapper;
    private Comparator<?> comparator;
    private Sort<T> previous;
    
    private Sort(Mapper<?, T> mapper, Comparator<?> comparator, Sort<T> previous) {
        this.previous = previous;
        this.mapper = mapper;
        this.comparator = comparator;
    }
    
    public static <T, R extends Comparable<? super R>> Sort<T> by(Mapper<R, T> mapper) {
        Objects.requireNonNull(mapper);
        return new Sort(mapper, Ordering.natural(), null);
    }
    
    public static <T, R> Sort<T> by(Mapper<R, T> mapper, Comparator<? super R> comparator) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(comparator);
        return new Sort(mapper, comparator, null);
    }
    
    @Override
    public int compare(T o1, T o2) {
        int result = 0;
        if (previous != null) {
            result = previous.compare(o1, o2);
        }
        if (result == 0) {
            // using cast here, but type safety is ensured by the factory methods
            result = ((Comparator<Object>) comparator).compare(mapper.map(o1), mapper.map(o2));
        }
        return result;
    }
    
    public <R extends Comparable<? super R>> Sort<T> thenBy(Mapper<R, T> mapper) {
        return new Sort(mapper, Ordering.natural(), this);
    }
    
    public <R> Sort<T> thenBy(Mapper<R, T> mapper, Comparator<? super R> comparator) {
        return new Sort(mapper, comparator, this);
    }
    
    public Ordering<T> toOrdering() {
        return Ordering.from(this);
    }
}