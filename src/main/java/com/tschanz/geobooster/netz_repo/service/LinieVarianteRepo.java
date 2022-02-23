package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;

import java.time.LocalDate;
import java.util.Collection;


public interface LinieVarianteRepo {
    Collection<VerkehrskanteVersion> searchVerkehrskanteVersions(Collection<Long> linienVarianteIds, LocalDate date, Extent<Epsg3857Coordinate> extent);

    Collection<TarifkanteVersion> searchTarifkanteVersions(Collection<Long> linienVarianteIds, LocalDate date, Extent<Epsg3857Coordinate> extent);
}
