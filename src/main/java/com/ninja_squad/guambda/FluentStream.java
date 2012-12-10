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
import java.util.function.BinaryOperator;
import java.util.function.Block;
import java.util.function.Combiner;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.FlatMapper;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Predicates;
import java.util.stream.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamShape;
import java.util.stream.Streamable;
import java.util.stream.primitive.IntStream;

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
    public <R> FluentStream<R> map(Function<? super T, ? extends R> mapper) {
        return FluentStream.from(stream.map(mapper));
    }

    @Override
    public IntStream map(IntFunction<? super T> mapper) {
        return stream.map(mapper);
    }

    @Override
    public FluentStream<T> skip(long n) {
        return FluentStream.from(stream.skip(n));
    }

    @Override
    public FluentStream<T> limit(long n) {
        return FluentStream.from(stream.limit(n));
    }

    @Override
    public FluentStream<T> slice(long skip, long limit) {
        return FluentStream.from(stream.slice(skip, limit));
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return stream.max(comparator);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return stream.min(comparator);
    }

    @Override
    public <R> FluentStream<R> flatMap(FlatMapper<? super T, ? extends R> mapper) {
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
    public <A extends Destination<? super T>> A into(A target) {
        target.addAll(this);
        return target;
    }

    @Override
    public Object[] toArray() {
        return stream.toArray();
    }

    @Override
    public <U> Map<U, Collection<T>> groupBy(Function<? super T, ? extends U> classifier) {
        return stream.groupBy(classifier);
    }

    @Override
    public <U, W> Map<U, W> reduceBy(Function<? super T, ? extends U> classifier,
                                     Supplier<W> baseFactory,
                                     Combiner<W, T, W> reducer,
                                     BinaryOperator<W> combiner) {
        return stream.reduceBy(classifier, baseFactory, reducer, combiner);
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
    public <U> U fold(Supplier<U> baseFactory,
                      Combiner<U, T, U> reducer,
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
    public int getStreamFlags() {
        return stream.getStreamFlags();
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
    public Spliterator<T> spliterator() {
        return stream.spliterator();
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
    public <R> FluentStream<R> map(IndexedFunction<? super T, ? extends R> mapper) {
        return FluentStream.from(stream.sequential().map(new IndexedMapperWrapper<>(mapper)));
    }
    
    // It's there because it's not provided by Lambda and Guava, and useful
    public <R> FluentStream<R> flatMap(IndexedFlatMapper<? super T, ? extends R> flatMapper) {
        return FluentStream.from(stream.sequential().flatMap(new IndexedFlatMapperWrapper<>(flatMapper)));
    }
    
    // It's there because FluentIterable has it, it's really useful, and Lambda doesn't have it
    public <K> ImmutableMap<K, T> uniqueIndex(Function<? super T, ? extends K> toKeyMapper) {
        ImmutableMap.Builder<K, T> builder = new ImmutableMap.Builder<>();
        forEach(t -> {
            builder.put(toKeyMapper.apply(t), t);
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
        return t == null ? Optional.<T>empty() : Optional.of(t);
    }
    
    // It's there because FluentIterable has it, and it's a useful shortcut
    public Optional<T> get(int position) {
        T t = com.google.common.collect.Iterators.get(iterator(), position, null);
        return t == null ? Optional.<T>empty() : Optional.of(t);
    }
    
    // It's there because it's a useful shortcut
    public <K, M extends Multimap<K, T>> M multimap(M target, Function<? super T, ? extends K> mapper) {
        return this.into(MultimapDestination.from(target, mapper)).getTarget();
    }

    private static final class IndexedBlockWrapper<T> implements Block<T> {
        private final IndexedBlock<? super T> indexedBlock;
        private int index = 0;
        
        private IndexedBlockWrapper(IndexedBlock<? super T> indexedBlock) {
            this.indexedBlock = indexedBlock;
        }
        
        @Override
        public void accept(T t) {
            indexedBlock.accept(index, t);
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
    
    private static final class IndexedMapperWrapper<R, T> implements Function<T, R> {
        private final IndexedFunction<? super T, ? extends R> indexedFunction;
        private int index = 0;
        
        private IndexedMapperWrapper(IndexedFunction<? super T, ? extends R> indexedFunction) {
            this.indexedFunction = indexedFunction;
        }
        
        @Override
        public R apply(T t) {
            R result = indexedFunction.apply(index, t);
            index++;
            return result;
        }
    }
    
    private static final class IndexedFlatMapperWrapper<T, R> implements FlatMapper<T, R> {
        private final IndexedFlatMapper<? super T, ? extends R> indexedFlatMapper;
        private int index = 0;
        
        private IndexedFlatMapperWrapper(IndexedFlatMapper<? super T, ? extends R> indexedFlatMapper) {
            this.indexedFlatMapper = indexedFlatMapper;
        }
        
        @Override
        public void flatMapInto(Block<? super R> sink, T t) {
            indexedFlatMapper.flatMapInto(sink, index, t);
            index++;
        }
    }
}