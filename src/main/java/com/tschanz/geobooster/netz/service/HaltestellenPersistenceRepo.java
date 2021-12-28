package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectPersistence;


public interface HaltestellenPersistenceRepo extends VersionedObjectPersistence<Haltestelle, HaltestelleVersion> {
}
