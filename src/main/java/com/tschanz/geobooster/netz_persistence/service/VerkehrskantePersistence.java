package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface VerkehrskantePersistence extends VersionedObjectPersistence<Verkehrskante, VerkehrskanteVersion> {
}
