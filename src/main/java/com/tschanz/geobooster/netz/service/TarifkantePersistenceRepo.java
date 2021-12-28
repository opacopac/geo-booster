package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectPersistence;


public interface TarifkantePersistenceRepo extends VersionedObjectPersistence<Tarifkante, TarifkanteVersion> {
}

