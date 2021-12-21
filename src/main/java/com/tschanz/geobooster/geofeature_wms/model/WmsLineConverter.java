package com.tschanz.geobooster.geofeature_wms.model;

import com.tschanz.geobooster.geofeature.model.Line;


public class WmsLineConverter {
    public static String toRest(Line line) {
        return String.format(
            "{\"type\": \"LineString\", \"coordinates\": %s }",
            WmsCoordinateConverter.toRestList(line.getCoordinateList())
        );
    }
}
