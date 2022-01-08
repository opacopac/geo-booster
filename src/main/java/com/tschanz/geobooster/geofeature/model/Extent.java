package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.NonNull;


@Getter
public class Extent<T extends Coordinate> {
    @NonNull private final T minCoordinate;
    @NonNull private final T maxCoordinate;


    public Extent(T minCoordinate, T maxCoordinate) {
        if (minCoordinate.getX() > maxCoordinate.getX() || minCoordinate.getY() > maxCoordinate.getY()) {
            throw new IllegalArgumentException("min coordinate value must be less than max coordinate value");
        }

        this.minCoordinate = minCoordinate;
        this.maxCoordinate = maxCoordinate;
    }
    

    public static Extent<Epsg3857Coordinate> fromEpsg3857Points(double e1, double n1, double e2, double n2) {
        return fromCoords(new Epsg3857Coordinate(e1, n1), new Epsg3857Coordinate(e2, n2));
    }


    public static Extent<Epsg4326Coordinate> fromEpsg4326Points(double lon1, double lat1, double lon2, double lat2) {
        return fromCoords(new Epsg4326Coordinate(lon1, lat1), new Epsg4326Coordinate(lon2, lat2));
    }


    public static Extent<Epsg3857Coordinate> fromCoords(Epsg3857Coordinate coord1, Epsg3857Coordinate coord2) {
        return new Extent<>(
            new Epsg3857Coordinate(Math.min(coord1.getE(), coord2.getE()), Math.min(coord1.getN(), coord2.getN())),
            new Epsg3857Coordinate(Math.max(coord1.getE(), coord2.getE()), Math.max(coord1.getN(), coord2.getN()))
        );
    }


    public static Extent<Epsg4326Coordinate> fromCoords(Epsg4326Coordinate coord1, Epsg4326Coordinate coord2) {
        return new Extent<>(
            new Epsg4326Coordinate(Math.min(coord1.getLongitude(), coord2.getLongitude()), Math.min(coord1.getLatitude(), coord2.getLatitude())),
            new Epsg4326Coordinate(Math.max(coord1.getLongitude(), coord2.getLongitude()), Math.max(coord1.getLatitude(), coord2.getLatitude()))
        );
    }


    public boolean isPointInside(Coordinate coordinate) {
        return coordinate.getX() >= this.minCoordinate.getX()
            && coordinate.getY() >= this.minCoordinate.getY()
            && coordinate.getX() < this.maxCoordinate.getX()
            && coordinate.getY() < this.maxCoordinate.getY();
    }


    public boolean isExtentInside(Extent<T> extent) {
        return extent.getMinCoordinate().getX() >= this.minCoordinate.getX() && extent.getMaxCoordinate().getX() <= this.maxCoordinate.getX()
            && extent.getMinCoordinate().getY() >= this.minCoordinate.getY() && extent.getMaxCoordinate().getY() <= this.maxCoordinate.getY();
    }


    public boolean isExtentIntersecting(Extent<T> extent) {
        if (extent.getMinCoordinate().getX() >= this.maxCoordinate.getX() || extent.getMaxCoordinate().getX() < this.minCoordinate.getX()) {
            return false;
        }

        if (extent.getMinCoordinate().getY() >= this.maxCoordinate.getY() || extent.getMaxCoordinate().getY() < this.minCoordinate.getY()) {
            return false;
        }

        return true;
    }
}
