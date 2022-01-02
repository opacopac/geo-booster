package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface TarifkantePersistence extends VersionedObjectPersistence<Tarifkante, TarifkanteVersion> {
}

