package com.ninja_squad.guambda;

import java.util.*;
import java.util.functions.*;
import com.google.common.collect.*;

public class Sort<T, R> implements Comparator<T> {
    private Mapper<R, T> mapper;
    private Comparator<? super R> comparator;
    private Sort<T, ?> previous;
    
    private Sort(Mapper<R, T> mapper, Comparator<? super R> comparator, Sort<T, ?> previous) {
        this.previous = previous;
        this.mapper = mapper;
        this.comparator = comparator;
    }
    
    public static <T, R extends Comparable<? super R>> Sort<T, R> by(Mapper<R, T> mapper) {
        Objects.requireNonNull(mapper);
        return new Sort(mapper, Ordering.natural(), null);
    }
    
    public static <T, R> Sort<T, R> by(Mapper<R, T> mapper, Comparator<? super R> comparator) {
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
        return result == 0 ? comparator.compare(mapper.map(o1), mapper.map(o2)) : result;
    }
    
    public <R2 extends Comparable<? super R2>> Sort<T, R2> thenBy(Mapper<R2, T> mapper) {
        return new Sort(mapper, Ordering.natural(), this);
    }
    
    public <R2> Sort<T, R2> thenBy(Mapper<R2, T> mapper, Comparator<? super R2> comparator) {
        return new Sort(mapper, comparator, this);
    }
    
    public Ordering<T> toOrdering() {
        return Ordering.from(this);
    }
}