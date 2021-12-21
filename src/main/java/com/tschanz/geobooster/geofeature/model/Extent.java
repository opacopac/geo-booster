package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Extent {
    @NonNull private final Coordinate minCoordinate;
    @NonNull private final Coordinate maxCoordinate;
}
