package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDate;
import java.util.Collection;


public interface TarifkantePersistence extends VersionedObjectPersistence<Tarifkante, TarifkanteVersion> {
    Collection<Tarifkante> readChangedElements(LocalDate changedSince);

    Collection<TarifkanteVersion> readChangedVersions(LocalDate changedSince);
}

