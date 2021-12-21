package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Point {
    @NonNull private final Coordinate coordinate;
}
