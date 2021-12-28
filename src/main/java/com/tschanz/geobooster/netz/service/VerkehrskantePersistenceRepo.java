package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectPersistence;


public interface VerkehrskantePersistenceRepo extends VersionedObjectPersistence<Verkehrskante, VerkehrskanteVersion> {
}
