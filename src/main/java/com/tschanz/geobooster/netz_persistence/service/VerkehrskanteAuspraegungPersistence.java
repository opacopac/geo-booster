package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface VerkehrskanteAuspraegungPersistence extends VersionedObjectPersistence<VerkehrskanteAuspraegung, VerkehrskanteAuspraegungVersion> {
}
