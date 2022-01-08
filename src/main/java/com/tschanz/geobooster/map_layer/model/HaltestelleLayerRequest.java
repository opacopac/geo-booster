package com.tschanz.geobooster.map_layer.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;

import java.time.LocalDate;


public interface HaltestelleLayerRequest {
    LocalDate getDate();
    Extent<Epsg3857Coordinate> getBbox();
}
