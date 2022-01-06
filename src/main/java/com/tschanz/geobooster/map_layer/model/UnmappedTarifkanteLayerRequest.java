package com.tschanz.geobooster.map_layer.model;

import com.tschanz.geobooster.geofeature.model.Extent;

import java.time.LocalDate;


public interface UnmappedTarifkanteLayerRequest {
    LocalDate getDate();
    Extent getBbox();
}
