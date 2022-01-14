package com.tschanz.geobooster.tarif_persistence.service;

import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDateTime;
import java.util.Collection;


public interface AwbPersistence extends VersionedObjectPersistence<Awb, AwbVersion> {
    ElementVersionChanges<Awb, AwbVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
