package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;


public class GetMapRequestExtentConverter {
    public static Extent<Epsg3857Coordinate> fromRest(String bbox, String projection) {
        var coords = bbox.split(",");

        switch (projection) {
            case Epsg3857Coordinate.PROJECTION:
                return Extent.fromCoords(
                    new Epsg3857Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])),
                    new Epsg3857Coordinate(Double.parseDouble(coords[2]), Double.parseDouble(coords[3]))
                );
            case Epsg4326Coordinate.PROJECTION:
                return Extent.fromCoords(
                    CoordinateConverter.convertToEpsg3857(
                        new Epsg4326Coordinate(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]))
                    ),
                    CoordinateConverter.convertToEpsg3857(
                        new Epsg4326Coordinate(Double.parseDouble(coords[2]), Double.parseDouble(coords[3]))
                    )
                );
            default:
                throw new IllegalArgumentException("Unsupported reference system '" + projection + "'");
        }
    }
}
