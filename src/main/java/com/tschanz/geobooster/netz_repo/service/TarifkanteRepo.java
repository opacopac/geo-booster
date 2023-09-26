package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;
import io.reactivex.subjects.PublishSubject;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface TarifkanteRepo extends VersionedObjectRepo<Tarifkante, TarifkanteVersion> {
    TarifkanteVersion getElementVersionAtDate(long elementId, LocalDate date);

    List<TarifkanteVersion> searchByExtent(Extent<Epsg3857Coordinate> extent);

    Haltestelle getStartHaltestelle(TarifkanteVersion tkVersion);

    Haltestelle getEndHaltestelle(TarifkanteVersion tkVersion);

    HaltestelleVersion getStartHaltestelleVersion(TarifkanteVersion tkVersion);

    HaltestelleVersion getEndHaltestelleVersion(TarifkanteVersion tkVersion);

    Epsg3857Coordinate getStartCoordinate(TarifkanteVersion tkVersion);

    Epsg3857Coordinate getEndCoordinate(TarifkanteVersion tkVersion);

    boolean hasOneOfVerwaltungAndVmTypes(TarifkanteVersion tkVersion, Collection<VerkehrsmittelTyp> vmTypes, Map<Long, Long> verwaltungIdMap);

    PublishSubject<Boolean> getTkUpdateSubject();
}
