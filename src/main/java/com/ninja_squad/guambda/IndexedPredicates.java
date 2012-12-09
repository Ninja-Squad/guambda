package com.ninja_squad.guambda;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.functions.Predicate;

public final class IndexedPredicates {
    private IndexedPredicates() {
    }
    
    public static <T> IndexedPredicate<T> wrap(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return (i, t) -> predicate.test(t);
    }
    
    public static <T> IndexedPredicate<T> negate(IndexedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return (i, t) -> !predicate.test(i, t);
    }
    
    public static <T> IndexedPredicate<T> or(IndexedPredicate<? super T> first, IndexedPredicate<? super T> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        return (i, t) -> first.test(i, t) || second.test(i, t);
    }
    
    public static <T> IndexedPredicate<T> and(IndexedPredicate<? super T> first, IndexedPredicate<? super T> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        return (i, t) -> first.test(i, t) && second.test(i, t);
    }
    
    
    public static <T> IndexedPredicate<T> xor(IndexedPredicate<? super T> first, IndexedPredicate<? super T> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        return (i, t) -> first.test(i, t) ^ second.test(i, t);
    }
    
    public static <T> IndexedPredicate<T> and(Iterable<? extends IndexedPredicate<? super T>> predicates) {
        Objects.requireNonNull(predicates);
        return (i, t) -> {
            for (IndexedPredicate<? super T> predicate : predicates) {
                if (!predicate.test(i, t)) {
                    return false;
                }
            }
            return true;
        };
    }

    @SafeVarargs
    public static <T> IndexedPredicate<T> and(IndexedPredicate<? super T>... predicates) {
        return and(Arrays.asList(predicates));
    }
    
    public static <T> IndexedPredicate<T> or(Iterable<? extends IndexedPredicate<? super T>> predicates) {
        Objects.requireNonNull(predicates);

        return (i, t) -> {
            for (IndexedPredicate<? super T> predicate : predicates) {
                if (predicate.test(i, t)) {
                    return true;
                }
            }
            return false;
        };
    }

    @SafeVarargs
    public static <T> IndexedPredicate<T> or(IndexedPredicate<? super T>... predicates) {
         return or(Arrays.asList(predicates));
     }

    public static <T> IndexedPredicate<T> xor(Iterable<? extends IndexedPredicate<? super T>> predicates) {
        Objects.requireNonNull(predicates);

        return (i, t) -> {
            Iterator<? extends IndexedPredicate<? super T>> iterator = predicates.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            boolean initial = iterator.next().test(i, t);
            while (iterator.hasNext()) {
                boolean current = iterator.next().test(i, t);
                if (!(initial ^ current)) {
                    return false;
                }
            }
            return true;
         };
    }

    @SafeVarargs
    public static <T> IndexedPredicate<T> xor(IndexedPredicate<? super T>... predicates) {
        return xor(Arrays.asList(predicates));
    }
}