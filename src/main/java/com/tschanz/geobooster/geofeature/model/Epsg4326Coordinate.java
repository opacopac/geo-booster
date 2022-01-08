package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Epsg4326Coordinate implements Coordinate {
    public final static String PROJECTION = "EPSG:4326";

    private final double longitude;
    private final double latitude;


    @Override
    public String getProjection() {
        return PROJECTION;
    }


    @Override
    public double getX() {
        return this.longitude;
    }

    @Override
    public double getY() {
        return this.latitude;
    }
}
