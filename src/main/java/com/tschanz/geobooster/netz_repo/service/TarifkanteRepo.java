package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.util.Collection;
import java.util.List;


public interface TarifkanteRepo extends VersionedObjectRepo<Tarifkante, TarifkanteVersion> {
    List<TarifkanteVersion> searchByExtent(Extent<Epsg3857Coordinate> extent);

    Haltestelle getStartHaltestelle(TarifkanteVersion tkVersion);

    Haltestelle getEndHaltestelle(TarifkanteVersion tkVersion);

    HaltestelleVersion getStartHaltestelleVersion(TarifkanteVersion tkVersion);

    HaltestelleVersion getEndHaltestelleVersion(TarifkanteVersion tkVersion);

    Epsg3857Coordinate getStartCoordinate(TarifkanteVersion tkVersion);

    Epsg3857Coordinate getEndCoordinate(TarifkanteVersion tkVersion);

    boolean hasOneOfVmTypes(TarifkanteVersion tkVersion, Collection<VerkehrsmittelTyp> vmTypes);

    boolean hasOneOfVerwaltungIds(TarifkanteVersion tkVersion, Collection<Long> verwaltungIds);
}
