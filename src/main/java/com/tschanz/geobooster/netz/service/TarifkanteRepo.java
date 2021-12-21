package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface TarifkanteRepo {
    List<TarifkanteVersion> readTarifkanteVersions(LocalDate date, Extent extent);

    Map<Long, Tarifkante> getTkElementMap();

    Map<Long, TarifkanteVersion> getTkVersionMap();
}
