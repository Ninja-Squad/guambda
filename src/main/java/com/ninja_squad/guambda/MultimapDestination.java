package com.ninja_squad.guambda;

import com.google.common.collect.Multimap;

import java.util.function.Function;
import java.util.stream.Stream;

public class MultimapDestination<K, V, M extends Multimap<K, V>> implements Stream.Destination<V> {
    private final M target;
    private final Function<? super V, ? extends K> mapper;
    
    private MultimapDestination(M target, Function<? super V, ? extends K> mapper) {
        this.target = target;
        this.mapper = mapper;
    }
    
    public static <K, V, M extends Multimap<K, V>> MultimapDestination<K, V, M> from(M target,
                                                                                     Function<? super V, ? extends K> mapper) {
        return new MultimapDestination<>(target, mapper);
    }
    
    @Override
    public void addAll(Stream<? extends V> stream) {
        stream.sequential().forEach((V v) -> {
            K key = mapper.apply(v);
            target.put(key, v);
        });
    }
    
    public M getTarget() {
        return target;
    }
}