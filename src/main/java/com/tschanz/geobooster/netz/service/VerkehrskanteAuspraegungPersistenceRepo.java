package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectPersistence;


public interface VerkehrskanteAuspraegungPersistenceRepo extends VersionedObjectPersistence<VerkehrskanteAuspraegung, VerkehrskanteAuspraegungVersion> {
}
