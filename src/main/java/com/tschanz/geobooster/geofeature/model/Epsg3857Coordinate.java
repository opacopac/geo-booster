package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Epsg3857Coordinate implements Coordinate {
    public final static String PROJECTION = "EPSG:3857";

    private final double e;
    private final double n;


    @Override
    public String getProjection() {
        return PROJECTION;
    }
}
