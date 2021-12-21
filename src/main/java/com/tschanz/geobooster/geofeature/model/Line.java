package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class Line {
    @NonNull private final Coordinate startCoordinate;
    @NonNull private final Coordinate endCoordinate;


    public List<Coordinate> getCoordinateList() {
        return Arrays.asList(startCoordinate, endCoordinate);
    }
}
