package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDateTime;
import java.util.Collection;


public interface TarifkantePersistence extends VersionedObjectPersistence<Tarifkante, TarifkanteVersion> {
    ElementVersionChanges<Tarifkante, TarifkanteVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
