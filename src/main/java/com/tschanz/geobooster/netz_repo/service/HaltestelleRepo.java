package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.List;


public interface HaltestelleRepo extends VersionedObjectRepo<Haltestelle, HaltestelleVersion> {
    List<HaltestelleVersion> searchVersions(LocalDate date, Extent extent);

    VersionedObjectMap<Haltestelle, HaltestelleVersion> getVersionedObjectMap();
}