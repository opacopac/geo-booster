package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import com.tschanz.geobooster.versioning.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.List;


public interface TarifkanteRepo extends VersionedObjectRepo<Tarifkante, TarifkanteVersion> {
    List<TarifkanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes);

    Haltestelle getStartHaltestelle(TarifkanteVersion tkVersion);

    Haltestelle getEndHaltestelle(TarifkanteVersion tkVersion);

    HaltestelleVersion getStartHaltestelleVersion(TarifkanteVersion tkVersion);

    HaltestelleVersion getEndHaltestelleVersion(TarifkanteVersion tkVersion);

    Epsg4326Coordinate getStartCoordinate(TarifkanteVersion tkVersion);

    Epsg4326Coordinate getEndCoordinate(TarifkanteVersion tkVersion);

    VersionedObjectMap<Tarifkante, TarifkanteVersion> getVersionedObjectMap();
}
