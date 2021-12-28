package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectPersistence;


public interface BetreiberPersistenceRepo extends VersionedObjectPersistence<Betreiber, BetreiberVersion> {
}
