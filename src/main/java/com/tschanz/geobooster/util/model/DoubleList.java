package com.tschanz.geobooster.util.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class DoubleList<T> {
    private final List<T> list1;
    private final List<T> List2;
}
