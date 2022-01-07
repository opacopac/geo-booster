package com.tschanz.geobooster.util.service;


import com.tschanz.geobooster.util.model.KeyValue;

import java.util.*;
import java.util.stream.Stream;


public class ArrayHelper {
    public static String[] appendTo(String[] originalArray, String... addValues) {
        return Stream.concat(
            Arrays.stream(originalArray),
            Arrays.stream(addValues)
        ).toArray(String[]::new);
    }


    public static <T,K> Map<T, K> create1to1LookupMap(Collection<KeyValue<T, K>> keyValueList) {
        Map<T, K> lookupMap = new HashMap<>();

        keyValueList.forEach(keyValue -> {
            lookupMap.put(keyValue.getKey(), keyValue.getValue());
        });

        return lookupMap;
    }


    public static <T,K> Map<T, Collection<K>> create1toNLookupMap(Collection<KeyValue<T, K>> keyValueList) {
        Map<T, Collection<K>> lookupMap = new HashMap<>();

        keyValueList.forEach(keyValue -> {
            var targetIdList = lookupMap.computeIfAbsent(keyValue.getKey(), k -> new ArrayList<>());
            targetIdList.add(keyValue.getValue());
        });

        return lookupMap;
    }
}
