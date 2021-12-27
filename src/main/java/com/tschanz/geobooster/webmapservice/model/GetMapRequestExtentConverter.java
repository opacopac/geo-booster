package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;


public class GetMapRequestExtentConverter {
    public static Extent fromRest(String bbox, String projection) {
        var coords = bbox.split(",");

        switch (projection) {
            case Epsg3857Coordinate.PROJECTION:
                return new Extent(
                    new Epsg3857Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])),
                    new Epsg3857Coordinate(Double.parseDouble(coords[2]), Double.parseDouble(coords[3]))
                );
            case Epsg4326Coordinate.PROJECTION:
                return new Extent(
                    new Epsg4326Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])),
                    new Epsg4326Coordinate(Double.parseDouble(coords[2]), Double.parseDouble(coords[3]))
                );
            default:
                throw new IllegalArgumentException("Unsupported reference system '" + projection + "'");
        }
    }
}
