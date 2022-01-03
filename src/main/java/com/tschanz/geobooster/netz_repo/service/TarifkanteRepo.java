package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.List;


public interface TarifkanteRepo extends VersionedObjectRepo<Tarifkante, TarifkanteVersion> {
    List<TarifkanteVersion> searchVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes, List<Long> verwaltungIds, boolean showUnmapped);

    Haltestelle getStartHaltestelle(TarifkanteVersion tkVersion);

    Haltestelle getEndHaltestelle(TarifkanteVersion tkVersion);

    HaltestelleVersion getStartHaltestelleVersion(TarifkanteVersion tkVersion);

    HaltestelleVersion getEndHaltestelleVersion(TarifkanteVersion tkVersion);

    Epsg4326Coordinate getStartCoordinate(TarifkanteVersion tkVersion);

    Epsg4326Coordinate getEndCoordinate(TarifkanteVersion tkVersion);

    VersionedObjectMap<Tarifkante, TarifkanteVersion> getVersionedObjectMap();
}
