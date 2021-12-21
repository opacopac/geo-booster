package com.tschanz.geobooster.geofeature_wms.model;

import com.tschanz.geobooster.geofeature.model.Point;


public class WmsPointConverter {
    public static String toRest(Point point) {
        return String.format(
            "{\"type\": \"Point\", \"coordinates\": %s }",
            WmsCoordinateConverter.toRest(point.getCoordinate())
        );
    }
}
