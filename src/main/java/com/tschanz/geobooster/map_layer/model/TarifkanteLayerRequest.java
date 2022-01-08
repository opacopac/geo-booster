package com.tschanz.geobooster.map_layer.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;

import java.time.LocalDate;
import java.util.Collection;


public interface TarifkanteLayerRequest {
    LocalDate getDate();
    Extent<Epsg3857Coordinate> getBbox();
    Collection<VerkehrsmittelTyp> getVmTypes();
    Collection<Long> getVerwaltungVersionIds();
    Collection<Long> getLinieVarianteIds();
    boolean isShowTerminiert();
}
