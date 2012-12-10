package com.ninja_squad.guambda;

import com.google.common.collect.Ordering;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Functions;

public final class Sort<T> implements Comparator<T> {
    private Function<? super T, ?> mapper;
    private Comparator<?> comparator;
    private Comparator<? super T> previous;
    
    private Sort(Function<? super T, ?> mapper, Comparator<?> comparator, Comparator<? super T> previous) {
        this.previous = previous;
        this.mapper = mapper;
        this.comparator = comparator;
    }
    
    public static <T, R extends Comparable<? super R>> Sort<T> by(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return new Sort(mapper, Ordering.natural(), null);
    }
    
    public static <T, R> Sort<T> by(Function<? super T, ? extends R> mapper, Comparator<? super R> comparator) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(comparator);
        return new Sort(mapper, comparator, null);
    }
    
    public static <T> Sort<T> with(Comparator<? super T> toWrap) {
        Objects.requireNonNull(toWrap);
        return new Sort(Functions.identity(), Ordering.allEqual(), toWrap);
    }

    @Override
    public int compare(T o1, T o2) {
        int result = 0;
        if (previous != null) {
            result = previous.compare(o1, o2);
        }
        if (result == 0) {
            // using cast here, but type safety is ensured by the factory methods
            result = ((Comparator<Object>) comparator).compare(mapper.apply(o1), mapper.apply(o2));
        }
        return result;
    }
    
    public <R extends Comparable<? super R>> Sort<T> thenBy(Function<? super T, ? extends R> mapper) {
        return new Sort(mapper, Ordering.natural(), this);
    }
    
    public <R> Sort<T> thenBy(Function<? super T, ? extends R> mapper, Comparator<? super R> comparator) {
        return new Sort(mapper, comparator, this);
    }
    
    public Ordering<T> toOrdering() {
        return Ordering.from(this);
    }
}