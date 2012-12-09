package com.ninja_squad.guambda;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.functions.BinaryOperator;
import java.util.functions.Block;
import java.util.functions.Combiner;
import java.util.functions.Factory;
import java.util.functions.FlatMapper;
import java.util.functions.Mapper;
import java.util.functions.Predicate;
import java.util.functions.Predicates;
import java.util.streams.Stream;
import java.util.streams.StreamShape;
import java.util.streams.Streamable;

public class FluentStream<T> implements Stream<T> {
    private final Stream<T> stream;
    
    private FluentStream(Stream<T> stream) {
        this.stream = stream;
    }
    
    public static <T> FluentStream<T> from(Stream<T> stream) {
        return new FluentStream<>(stream);
    }
    
    public static <T> FluentStream<T> from(Streamable<Stream<T>> streamable) {
        return new FluentStream<>(streamable.stream());
    }
    
    @Override
    public FluentStream<T> filter(Predicate<? super T> predicate) {
        return FluentStream.from(stream.filter(predicate));
    }
    
    @Override
    public void forEach(Block<? super T> block) {
        stream.forEach(block);
    }
    
    @Override
    public <R> FluentStream<R> map(Mapper<? extends R, ? super T> mapper) {
        return FluentStream.from(stream.map(mapper));
    }

    @Override
    public <R> FluentStream<R> flatMap(FlatMapper<? extends R, ? super T> mapper) {
        return FluentStream.from(stream.flatMap(mapper));
    }

    @Override
    public FluentStream<T> uniqueElements() {
        return FluentStream.from(stream.uniqueElements());
    }

    @Override
    public FluentStream<T> sorted(Comparator<? super T> comparator) {
        return FluentStream.from(stream.sorted(comparator));
    }

    @Override
    public FluentStream<T> cumulate(BinaryOperator<T> operator) {
        return FluentStream.from(stream.cumulate(operator));
    }

    @Override
    public FluentStream<T> tee(Block<? super T> block) {
        return FluentStream.from(stream.tee(block));
    }

    @Override
    public FluentStream<T> limit(int n) {
        return FluentStream.from(stream.limit(n));
    }

    @Override
    public FluentStream<T> skip(int n) {
        return FluentStream.from(stream.skip(n));
    }

    @Override
    public FluentStream<T> concat(Stream<? extends T> other) {
        return FluentStream.from(stream.concat(other));
    }

    @Override
    public <A extends Destination<? super T>> A into(A target) {
        target.addAll(this);
        return target;
    }

    @Override
    public Object[] toArray() {
        return stream.toArray();
    }

    @Override
    public <U> Map<U, Collection<T>> groupBy(Mapper<? extends U, ? super T> classifier) {
        return stream.groupBy(classifier);
    }

    @Override
    public <U, W> Map<U, W> reduceBy(Mapper<? extends U, ? super T> classifier,
                                     Factory<W> baseFactory,
                                     Combiner<W, W, T> reducer) {
        return stream.reduceBy(classifier, baseFactory, reducer);
    }

    @Override
    public T reduce(T base, BinaryOperator<T> op) {
        return stream.reduce(base, op);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> op) {
        return stream.reduce(op);
    }

    @Override
    public <U> U fold(Factory<U> baseFactory,
                      Combiner<U, U, T> reducer,
                      BinaryOperator<U> combiner) {
        return stream.fold(baseFactory, reducer, combiner);
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return stream.findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return stream.findAny();
    }

    @Override
    public FluentStream<T> sequential() {
        return FluentStream.from(stream.sequential());
    }

    @Override
    public FluentStream<T> unordered() {
        return FluentStream.from(stream.unordered());
    }
    
    @Override 
    public StreamShape getShape() {
        return stream.getShape();
    }
    
    @Override 
    public Iterator<T> iterator() {
        return stream.iterator();
    }
    
    @Override 
    public boolean isParallel() {
        return stream.isParallel();
    }
    
    @Override
    public String toString() {
        return stream.toString();
    }
    
    // It's there because it's not provided by Lambda and Guava, and useful
    public void forEach(IndexedBlock<? super T> block) {
        stream.sequential().forEach(new IndexedBlockWrapper<>(block));
    }
    
    // It's there because it's not provided by Lambda and Guava, and useful
    public FluentStream<T> tee(IndexedBlock<? super T> block) {
        return FluentStream.from(stream.sequential().tee(new IndexedBlockWrapper<>(block)));
    }
    
    // It's there because it's not provided by Lambda and Guava, and useful
    public FluentStream<T> filter(IndexedPredicate<? super T> predicate) {
        return FluentStream.from(stream.sequential().filter(new IndexedPredicateWrapper<>(predicate)));
    }
    
    // It's there because it's not provided by Lambda and Guava, and useful
    public <R> FluentStream<R> map(IndexedMapper<? extends R, ? super T> mapper) {
        return FluentStream.from(stream.sequential().map(new IndexedMapperWrapper<>(mapper)));
    }
    
    // It's there because it's not provided by Lambda and Guava, and useful
    public <R> FluentStream<R> flatMap(IndexedFlatMapper<? extends R, ? super T> flatMapper) {
        return FluentStream.from(stream.sequential().flatMap(new IndexedFlatMapperWrapper<>(flatMapper)));
    }
    
    // It's there because FluentIterable has it, it's really useful, and Lambda doesn't have it
    public <K> ImmutableMap<K, T> uniqueIndex(Mapper<? extends K, ? super T> toKeyMapper) {
        ImmutableMap.Builder<K, T> builder = new ImmutableMap.Builder<>();
        forEach(t -> {
            builder.put(toKeyMapper.map(t), t);
        });
        return builder.build();
    }
    
    // It's there because FluentIterable has it, it's really useful, and Lambda doesn't have it
    public ImmutableList<T> toList() {
        ImmutableList.Builder<T> builder = new ImmutableList.Builder<>();
        forEach(t -> {
            builder.add(t);
        });
        return builder.build();
    }
    
    // It's there because FluentIterable has it, it's really useful, and Lambda doesn't have it
    public ImmutableSet<T> toSet() {
        ImmutableSet.Builder<T> builder = new ImmutableSet.Builder<>();
        forEach(t -> {
            builder.add(t);
        });
        return builder.build();
    }
    
    // It's there because FluentIterable has it, it's really useful, and Lambda doesn't have it
    public ImmutableSortedSet<T> toSortedSet(Comparator<? super T> comparator) {
        ImmutableSortedSet.Builder<T> builder = new ImmutableSortedSet.Builder<>(comparator);
        forEach(t -> {
            builder.add(t);
        });
        return builder.build();}
    
    // It's there because FluentIterable has it, it's a useful shortcut, more readable than the equivalent anyMatch(isEqual(o))
    public boolean contains(Object o) {
        return this.anyMatch(Predicates.<T>isEqual(o));
    }
    
    // It's there because FluentIterable has it, it's a useful shortcut, but not really more readable than filter(predicat).findFirst(). To remove?
    public Optional<T> firstMatch(Predicate<? super T> predicate) {
        return filter(predicate).findFirst();
    }
    
    // It's there because FluentIterable has it, and it's a useful shortcut
    public int size() {
        return com.google.common.collect.Iterators.size(iterator());
    }
    
    // It's there because FluentIterable has it, and it's a useful shortcut
    public Optional<T> findLast() {
        T t = com.google.common.collect.Iterators.getLast(iterator(), null);
        return t == null ? Optional.<T>empty() : new Optional<T>(t);
    }
    
    // It's there because FluentIterable has it, and it's a useful shortcut
    public Optional<T> get(int position) {
        T t = com.google.common.collect.Iterators.get(iterator(), position, null);
        return t == null ? Optional.<T>empty() : new Optional<>(t);
    }
    
    // It's there because it's a useful shortcut
    public <K, M extends Multimap<K, T>> M multimap(M target, Mapper<? extends K, ? super T> mapper) {
        return this.into(MultimapDestination.from(target, mapper)).getTarget();
    }

    private static final class IndexedBlockWrapper<T> implements Block<T> {
        private final IndexedBlock<? super T> indexedBlock;
        private int index = 0;
        
        private IndexedBlockWrapper(IndexedBlock<? super T> indexedBlock) {
            this.indexedBlock = indexedBlock;
        }
        
        @Override
        public void apply(T t) {
            indexedBlock.apply(index, t);
            index++;
        }
    }
    
    private static final class IndexedPredicateWrapper<T> implements Predicate<T> {
        private final IndexedPredicate<? super T> indexedPredicate;
        private int index = 0;
        
        private IndexedPredicateWrapper(IndexedPredicate<? super T> indexedPredicate) {
            this.indexedPredicate = indexedPredicate;
        }
        
        @Override
        public boolean test(T t) {
            boolean result = indexedPredicate.test(index, t);
            index++;
            return result;
        }
    }
    
    private static final class IndexedMapperWrapper<R, T> implements Mapper<R, T> {
        private final IndexedMapper<? extends R, ? super T> indexedMapper;
        private int index = 0;
        
        private IndexedMapperWrapper(IndexedMapper<? extends R, ? super T> indexedMapper) {
            this.indexedMapper = indexedMapper;
        }
        
        @Override
        public R map(T t) {
            R result = indexedMapper.map(index, t);
            index++;
            return result;
        }
    }
    
    private static final class IndexedFlatMapperWrapper<R, T> implements FlatMapper<R, T> {
        private final IndexedFlatMapper<? extends R, ? super T> indexedFlatMapper;
        private int index = 0;
        
        private IndexedFlatMapperWrapper(IndexedFlatMapper<? extends R, ? super T> indexedFlatMapper) {
            this.indexedFlatMapper = indexedFlatMapper;
        }
        
        @Override
        public void flatMapInto(Block<? super R> sink, T t) {
            indexedFlatMapper.flatMapInto(sink, index, t);
            index++;
        }
    }
}