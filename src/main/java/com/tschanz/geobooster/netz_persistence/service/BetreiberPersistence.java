package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface BetreiberPersistence extends VersionedObjectPersistence<Betreiber, BetreiberVersion> {
}
