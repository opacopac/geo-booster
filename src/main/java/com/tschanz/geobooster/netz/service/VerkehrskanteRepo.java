package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import com.tschanz.geobooster.versioning.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.List;


public interface VerkehrskanteRepo extends VersionedObjectRepo<Verkehrskante, VerkehrskanteVersion> {
    List<VerkehrskanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes);

    Haltestelle getStartHaltestelle(VerkehrskanteVersion vkVersion);

    Haltestelle getEndHaltestelle(VerkehrskanteVersion vkVersion);

    HaltestelleVersion getStartHaltestelleVersion(VerkehrskanteVersion vkVersion);

    HaltestelleVersion getEndHaltestelleVersion(VerkehrskanteVersion vkVersion);

    Epsg4326Coordinate getStartCoordinate(VerkehrskanteVersion vkVersion);

    Epsg4326Coordinate getEndCoordinate(VerkehrskanteVersion vkVersion);

    VersionedObjectMap<Verkehrskante, VerkehrskanteVersion> getVersionedObjectMap();
}
