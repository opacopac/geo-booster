package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.util.List;


public interface TarifkanteRepo extends VersionedObjectRepo<Tarifkante, TarifkanteVersion> {
    List<TarifkanteVersion> searchByExtent(Extent extent);

    Haltestelle getStartHaltestelle(TarifkanteVersion tkVersion);

    Haltestelle getEndHaltestelle(TarifkanteVersion tkVersion);

    HaltestelleVersion getStartHaltestelleVersion(TarifkanteVersion tkVersion);

    HaltestelleVersion getEndHaltestelleVersion(TarifkanteVersion tkVersion);

    Epsg4326Coordinate getStartCoordinate(TarifkanteVersion tkVersion);

    Epsg4326Coordinate getEndCoordinate(TarifkanteVersion tkVersion);
}
