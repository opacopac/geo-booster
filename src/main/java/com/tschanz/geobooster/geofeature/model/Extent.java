package com.tschanz.geobooster.geofeature.model;

import lombok.Getter;
import lombok.NonNull;


@Getter
public class Extent {
    @NonNull private final Coordinate minCoordinate;
    @NonNull private final Coordinate maxCoordinate;


    public Extent(Coordinate minCoordinate, Coordinate maxCoordinate) {
        this.checkSameProjOrThrow(minCoordinate, maxCoordinate);

        if (minCoordinate.getX() > maxCoordinate.getX() || minCoordinate.getY() > maxCoordinate.getY()) {
            throw new IllegalArgumentException("min coordinate value must be less than max coordinate value");
        }

        this.minCoordinate = minCoordinate;
        this.maxCoordinate = maxCoordinate;
    }


    public static Extent fromAny2Coords(Epsg3857Coordinate coord1, Epsg3857Coordinate coord2) {
        return new Extent(
            new Epsg3857Coordinate(Math.min(coord1.getE(), coord2.getE()), Math.min(coord1.getN(), coord2.getN())),
            new Epsg3857Coordinate(Math.max(coord1.getE(), coord2.getE()), Math.max(coord1.getN(), coord2.getN()))
        );
    }


    public static Extent fromAny2Coords(Epsg4326Coordinate coord1, Epsg4326Coordinate coord2) {
        return new Extent(
            new Epsg4326Coordinate(Math.min(coord1.getLongitude(), coord2.getLongitude()), Math.min(coord1.getLatitude(), coord2.getLatitude())),
            new Epsg4326Coordinate(Math.max(coord1.getLongitude(), coord2.getLongitude()), Math.max(coord1.getLatitude(), coord2.getLatitude()))
        );
    }


    public boolean isPointInside(Coordinate coordinate) {
        this.checkSameProjOrThrow(coordinate, this.minCoordinate);

        return coordinate.getX() >= this.minCoordinate.getX()
            && coordinate.getY() >= this.minCoordinate.getY()
            && coordinate.getX() < this.maxCoordinate.getX()
            && coordinate.getY() < this.maxCoordinate.getY();
    }


    public boolean isExtentInside(Extent extent) {
        this.checkSameProjOrThrow(extent.getMinCoordinate(), this.minCoordinate);

        return extent.getMinCoordinate().getX() >= this.minCoordinate.getX() && extent.getMaxCoordinate().getX() <= this.maxCoordinate.getX()
            && extent.getMinCoordinate().getY() >= this.minCoordinate.getY() && extent.getMaxCoordinate().getY() <= this.maxCoordinate.getY();
    }


    public boolean isExtentIntersecting(Extent extent) {
        this.checkSameProjOrThrow(extent.getMinCoordinate(), this.minCoordinate);

        if (extent.getMinCoordinate().getX() >= this.maxCoordinate.getX() || extent.getMaxCoordinate().getX() < this.minCoordinate.getX()) {
            return false;
        }

        if (extent.getMinCoordinate().getY() >= this.maxCoordinate.getY() || extent.getMaxCoordinate().getY() < this.minCoordinate.getY()) {
            return false;
        }

        return true;
    }


    private void checkSameProjOrThrow(Coordinate coord1, Coordinate coord2) {
        // using != instead of equals to compare objects
        if (coord1.getProjection() != (coord2.getProjection())) {
            throw new IllegalArgumentException("both coordinates must have the same projection");
        }
    }
}
