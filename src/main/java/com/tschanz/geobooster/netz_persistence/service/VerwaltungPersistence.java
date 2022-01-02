package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface VerwaltungPersistence extends VersionedObjectPersistence<Verwaltung, VerwaltungVersion> {
}

