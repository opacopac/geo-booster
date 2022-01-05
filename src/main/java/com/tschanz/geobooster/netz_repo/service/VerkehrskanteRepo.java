package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public interface VerkehrskanteRepo extends VersionedObjectRepo<Verkehrskante, VerkehrskanteVersion> {
    List<VerkehrskanteVersion> searchVersionsByExtent(
        LocalDate date,
        Extent extent,
        Collection<VerkehrsmittelTyp> vmTypes,
        Collection<Long> verwaltungIds,
        boolean showTerminiert
    );

    Haltestelle getStartHaltestelle(VerkehrskanteVersion vkVersion);

    Haltestelle getEndHaltestelle(VerkehrskanteVersion vkVersion);

    HaltestelleVersion getStartHaltestelleVersion(VerkehrskanteVersion vkVersion);

    HaltestelleVersion getEndHaltestelleVersion(VerkehrskanteVersion vkVersion);

    Epsg4326Coordinate getStartCoordinate(VerkehrskanteVersion vkVersion);

    Epsg4326Coordinate getEndCoordinate(VerkehrskanteVersion vkVersion);
}
