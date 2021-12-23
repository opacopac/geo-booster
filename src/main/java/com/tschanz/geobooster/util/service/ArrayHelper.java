package com.tschanz.geobooster.util.service;


import java.util.Arrays;
import java.util.stream.Stream;


public class ArrayHelper {
    public static String[] appendTo(String[] originalArray, String... addValues) {
        return Stream.concat(
            Arrays.stream(originalArray),
            Arrays.stream(addValues)
        ).toArray(String[]::new);
    }
}
