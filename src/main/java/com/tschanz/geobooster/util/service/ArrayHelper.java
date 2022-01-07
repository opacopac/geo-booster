package com.tschanz.geobooster.util.service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;


public class ArrayHelper {
    public static String[] appendTo(String[] originalArray, String... addValues) {
        return Stream.concat(
            Arrays.stream(originalArray),
            Arrays.stream(addValues)
        ).toArray(String[]::new);
    }


    public static <T, K, R> Map<K, R> create1to1LookupMap(Collection<T> collection, Function<T, K> getKeyFn, Function<T, R> getValueFn) {
        Map<K, R> lookupMap = new HashMap<>();

        collection.forEach(item -> {
            lookupMap.put(getKeyFn.apply(item), getValueFn.apply(item));
        });

        return lookupMap;
    }


    public static <T, K, R> Map<K, Collection<R>> create1toNLookupMap(Collection<T> collection, Function<T, K> getKeyFn, Function<T, R> getValueFn) {
        Map<K, Collection<R>> lookupMap = new HashMap<>();

        collection.forEach(item -> {
            var targetIdList = lookupMap.computeIfAbsent(getKeyFn.apply(item), k -> new ArrayList<>());
            targetIdList.add(getValueFn.apply(item));
        });

        return lookupMap;
    }
}
