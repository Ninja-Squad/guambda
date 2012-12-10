package com.ninja_squad.guambda;

public interface IndexedPredicate<T> {
    boolean test(int index, T t);
        
    default IndexedPredicate<T> negate() {
        return IndexedPredicates.negate(this);
    }
    
    default IndexedPredicate<T> or(IndexedPredicate<? super T> predicate) {
        return IndexedPredicates.or(this, predicate);
    }
    
    default IndexedPredicate<T> and(IndexedPredicate<? super T> predicate) {
        return IndexedPredicates.and(this, predicate);
    }
    
    default IndexedPredicate<T> xor(IndexedPredicate<? super T> predicate) {
        return IndexedPredicates.xor(this, predicate);
    }
}
