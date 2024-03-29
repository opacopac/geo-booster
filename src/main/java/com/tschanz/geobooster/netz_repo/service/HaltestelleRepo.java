package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.List;


public interface HaltestelleRepo extends VersionedObjectRepo<Haltestelle, HaltestelleVersion> {
    HaltestelleVersion getElementVersionAtDate(long elementId, LocalDate date);

    List<HaltestelleVersion> searchByExtent(Extent<Epsg3857Coordinate> extent);

    Haltestelle getByUic(int uicCode);
}
