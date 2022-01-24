package com.tschanz.geobooster.map_layer.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;

import java.time.LocalDate;
import java.util.Collection;


public interface UnmappedTarifkanteLayerRequest {
    LocalDate getDate();
    Extent<Epsg3857Coordinate> getBbox();
    Collection<Long> getVerwaltungVersionIds();
}
