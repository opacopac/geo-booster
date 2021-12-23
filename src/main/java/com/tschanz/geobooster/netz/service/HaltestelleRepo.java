package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;

import java.time.LocalDate;
import java.util.List;


public interface HaltestelleRepo {
    List<HaltestelleVersion> readVersions(LocalDate date, Extent extent);
}
