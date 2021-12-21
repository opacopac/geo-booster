package com.tschanz.geobooster.geofeature_wms.model;

import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;

import java.util.List;
import java.util.stream.Collectors;


public class WmsCoordinateConverter {
    public static String toRest(Coordinate coordinate) {
        var lonLat = CoordinateConverter.convertToEpsg4326(coordinate);
        return String.format("[%f,%f]", lonLat.getLongitude(), lonLat.getLatitude());
    }


    public static String toRestList(List<Coordinate> coordinateList) {
        return coordinateList.stream()
            .map(WmsCoordinateConverter::toRest)
            .collect(Collectors.joining(","));
    }
}
