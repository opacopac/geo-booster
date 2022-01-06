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


    public static <T,K> Map<T, Collection<K>> createLookupMap(Collection<KeyValue<T, K>> keyValueList) {
        Map<T, Collection<K>> lookupMap = new HashMap<>();

        keyValueList.forEach(keyValue -> {
            var targetIdList = lookupMap.computeIfAbsent(keyValue.getKey(), k -> new ArrayList<>());
            targetIdList.add(keyValue.getValue());
        });

        return lookupMap;
    }
}
