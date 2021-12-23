package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface VerkehrskanteAuspraegungRepo {
    List<VerkehrskanteAuspraegungVersion> readVersions(LocalDate date, Extent extent);

    Map<Long, VerkehrskanteAuspraegung> getElementMap();

    Map<Long, VerkehrskanteAuspraegungVersion> getVersionMap();
}
