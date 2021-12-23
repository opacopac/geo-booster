package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface HaltestelleRepo {
    List<HaltestelleVersion> readVersions(LocalDate date, Extent extent);

    Map<Long, Haltestelle> getElementMap();

    Map<Long, HaltestelleVersion> getVersionMap();
}
