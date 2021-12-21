package com.tschanz.geobooster.geofeature.service;

import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;


public class CoordinateConverter {
    private static final double a = 6378137.0;


    public static Epsg4326Coordinate convertEpsg3857To4326(Epsg3857Coordinate epsg3857Coordinate) {
        // D = -N / a
        // φ = π / 2 – 2 * atan(e ^ D)
        // λ = E / a

        var d = -epsg3857Coordinate.getN() / a;
        var phi = Math.PI / 2 - 2 * Math.atan(Math.exp(d));
        var lambda = epsg3857Coordinate.getE() / a;
        var lat = phi / Math.PI * 180;
        var lon = lambda / Math.PI * 180;

        return new Epsg4326Coordinate(lon, lat);
    }


    public static Epsg3857Coordinate convertEpsg4326to3857(Epsg4326Coordinate epsg4326Coordinate) {
        // E = a * λ
        // N = a * ln[tan(π/4 + φ/2)]

        var lambda = epsg4326Coordinate.getLongitude() / 180 * Math.PI;
        var phi = epsg4326Coordinate.getLatitude() / 180 * Math.PI;
        var e = a * lambda;
        var n = a * Math.log(Math.tan(Math.PI / 4 + phi / 2));

        return new Epsg3857Coordinate(e, n);
    }


    public static Epsg3857Coordinate convertToEpsg3857(Coordinate coordinate) {
        switch (coordinate.getProjection()) {
            case Epsg3857Coordinate.PROJECTION:
                return (Epsg3857Coordinate) coordinate;
            case Epsg4326Coordinate.PROJECTION:
                return convertEpsg4326to3857((Epsg4326Coordinate) coordinate);
            default:
                throw new UnsupportedOperationException(String.format("Coordinate system %s not supported", coordinate.getProjection()));
        }
    }


    public static Epsg4326Coordinate convertToEpsg4326(Coordinate coordinate) {
        switch (coordinate.getProjection()) {
            case Epsg3857Coordinate.PROJECTION:
                return convertEpsg3857To4326((Epsg3857Coordinate) coordinate);
            case Epsg4326Coordinate.PROJECTION:
                return (Epsg4326Coordinate) coordinate;
            default:
                throw new UnsupportedOperationException(String.format("Coordinate system %s not supported", coordinate.getProjection()));
        }
    }
}
