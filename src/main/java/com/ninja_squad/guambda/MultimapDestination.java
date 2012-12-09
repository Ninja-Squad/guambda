package com.ninja_squad.guambda;

import com.google.common.collect.Multimap;

import java.util.functions.Mapper;
import java.util.streams.Stream;

public class MultimapDestination<K, V, M extends Multimap<K, V>> implements Stream.Destination<V> {
    private final M target;
    private final Mapper<? extends K, ? super V> mapper;
    
    private MultimapDestination(M target, Mapper<? extends K, ? super V> mapper) {
        this.target = target;
        this.mapper = mapper;
    }
    
    public static <K, V, M extends Multimap<K, V>> MultimapDestination<K, V, M> from(M target,
                                                                                     Mapper<? extends K, ? super V> mapper) {
        return new MultimapDestination<>(target, mapper);
    }
    
    @Override
    public void addAll(Stream<? extends V> stream) {
        stream.forEach((V v) -> {
            K key = mapper.map(v);
            target.put(key, v);
        });
    }
    
    public M getTarget() {
        return target;
    }
}