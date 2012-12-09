package com.ninja_squad.guambda;

public interface IndexedPredicate<T> {
    boolean test(int index, T t);
        
    IndexedPredicate<T> negate() default {
        return IndexedPredicates.negate(this);
    }
    
    IndexedPredicate<T> or(IndexedPredicate<? super T> predicate) default {
        return IndexedPredicates.or(this, predicate);
    }
    
    IndexedPredicate<T> and(IndexedPredicate<? super T> predicate) default {
        return IndexedPredicates.and(this, predicate);
    }
    
    IndexedPredicate<T> xor(IndexedPredicate<? super T> predicate) default {
        return IndexedPredicates.xor(this, predicate);
    }
}
