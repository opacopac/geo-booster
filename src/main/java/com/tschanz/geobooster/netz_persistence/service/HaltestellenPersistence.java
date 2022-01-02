package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface HaltestellenPersistence extends VersionedObjectPersistence<Haltestelle, HaltestelleVersion> {
}
