package com.tschanz.geobooster.quadtree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QuadTreeExtent {
    private final QuadTreeCoordinate minCoordinate;
    private final QuadTreeCoordinate maxCoordinate;


    public boolean isCoordinateInside(QuadTreeCoordinate coordinate) {
        return coordinate.getX() >= this.minCoordinate.getX()
            && coordinate.getY() >= this.minCoordinate.getY()
            && coordinate.getX() < this.maxCoordinate.getX()
            && coordinate.getY() < this.maxCoordinate.getY();
    }


    public boolean isIntersecting(QuadTreeExtent extent) {
        if (extent.minCoordinate.getX() >= this.maxCoordinate.getX() || extent.maxCoordinate.getX() < this.minCoordinate.getX()) {
            return false;
        }

        if (extent.minCoordinate.getY() >= this.maxCoordinate.getY() || extent.maxCoordinate.getY() < this.minCoordinate.getY()) {
            return false;
        }

        return true;
    }
}
