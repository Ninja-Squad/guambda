package com.ninja_squad.guambda;

import com.google.common.collect.Ordering;

import java.util.Comparator;
import java.util.Comparators;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Functions;

public final class Sort<T> implements Comparator<T> {
    private Comparator<? super T> previous;
    private Function<? super T, ?> mapper;
    private Comparator<?> mappedValueComparator;

    private Sort(Comparator<? super T> previous, Function<? super T, ?> mapper, Comparator<?> mappedValueComparator) {
        this.previous = previous;
        this.mapper = mapper;
        this.mappedValueComparator = mappedValueComparator;
    }
    
    public static <T, R extends Comparable<? super R>> Sort<T> by(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return new Sort(null, mapper, Comparators.naturalOrder());
    }
    
    public static <T, R> Sort<T> by(Function<? super T, ? extends R> mapper, Comparator<? super R> mappedValueComparator) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(mappedValueComparator);
        return new Sort(null, mapper, mappedValueComparator);
    }
    
    public static <T> Sort<T> with(Comparator<? super T> toWrap) {
        Objects.requireNonNull(toWrap);
        return new Sort(null, Functions.identity(), toWrap);
    }

    @Override
    public int compare(T o1, T o2) {
        int result = 0;
        if (previous != null) {
            result = previous.compare(o1, o2);
        }
        if (result == 0) {
            // using cast here, but type safety is ensured by the factory methods
            result = ((Comparator<Object>) mappedValueComparator).compare(mapper.apply(o1), mapper.apply(o2));
        }
        return result;
    }
    
    public <R extends Comparable<? super R>> Sort<T> thenBy(Function<? super T, ? extends R> mapper) {
        return new Sort(this, mapper, Ordering.natural());
    }
    
    public <R> Sort<T> thenBy(Function<? super T, ? extends R> mapper, Comparator<? super R> comparator) {
        return new Sort(this, mapper, comparator);
    }

    public <R> Sort<T> thenWith(Comparator<? super T> comparator) {
        return new Sort(this, Functions.identity(), comparator);
    }

    public Ordering<T> toOrdering() {
        return Ordering.from(this);
    }
}