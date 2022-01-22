package com.tschanz.geobooster.util.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Tuple2<T, K> {
    private final T first;
    private final K second;
}
