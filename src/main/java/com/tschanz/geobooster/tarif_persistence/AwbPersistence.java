package com.tschanz.geobooster.tarif_persistence;

import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface AwbPersistence extends VersionedObjectPersistence<Awb, AwbVersion> {
}
