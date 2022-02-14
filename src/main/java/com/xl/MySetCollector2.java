package com.xl;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MySetCollector2<T> implements Collector<T, Set<T>, Map<T, T>> {
    @Override
    public Supplier<Set<T>> supplier() {
        System.out.println("supplier invoked");
        return HashSet::new;
    }

    @Override
    public BiConsumer<Set<T>, T> accumulator() {
        System.out.println("accumulator invoked");
        return Set<T>::add;
    }

    @Override
    public BinaryOperator<Set<T>> combiner() {
        System.out.println("combiner invoked");
        return (set1, set2) -> {
            System.out.println(Thread.currentThread().getName());
            set1.addAll(set2);
            return set1;
        };
    }

    @Override
    public Function<Set<T>, Map<T, T>> finisher() {
        System.out.println("finisher invoked");
        Map<T, T> map = new HashMap<>();
        return (set) -> {
            set.forEach(t -> map.put(t, t));
            return map;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        System.out.println("characteristics invoked");
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));
    }

    @Test
    public void test() {
        List<String> list = Arrays.asList("hello", "world", "welcome", "hello", "a", "b", "c");
        Set<String> set = new HashSet<>();
        set.addAll(list);
        System.out.println(set);
        Map<String, String> map = set.parallelStream().collect(new MySetCollector2<>());
        System.out.println(map);

    }
}
