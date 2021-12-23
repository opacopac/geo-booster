package com.tschanz.geobooster.util.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class KeyValue<T, K> {
    private final T key;
    private final K value;
}
