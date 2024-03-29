package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.List;


public interface VerkehrskanteRepo extends VersionedObjectRepo<Verkehrskante, VerkehrskanteVersion> {
    VerkehrskanteVersion getElementVersionAtDate(long elementId, LocalDate date);

    List<VerkehrskanteVersion> searchByExtent(Extent<Epsg3857Coordinate> extent);

    Haltestelle getStartHaltestelle(VerkehrskanteVersion vkVersion);

    Haltestelle getEndHaltestelle(VerkehrskanteVersion vkVersion);

    HaltestelleVersion getStartHaltestelleVersion(VerkehrskanteVersion vkVersion);

    HaltestelleVersion getEndHaltestelleVersion(VerkehrskanteVersion vkVersion);

    Epsg3857Coordinate getStartCoordinate(VerkehrskanteVersion vkVersion);

    Epsg3857Coordinate getEndCoordinate(VerkehrskanteVersion vkVersion);
}
