package com.tschanz.geobooster.tarif_persistence.service;

import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbIncVerwaltung;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.util.model.Tuple2;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDateTime;
import java.util.Collection;


public interface AwbPersistence extends VersionedObjectPersistence<Awb, AwbVersion> {
    Collection<AwbIncVerwaltung> readAllAwbIncVerwaltungen();

    ElementVersionChanges<Awb, AwbVersion> findAwbVersionChanges(LocalDateTime changedSince, Collection<Long> currentIds);

    Tuple2<Collection<AwbIncVerwaltung>, Collection<Long>> findAwbIncVerwaltungChanges(LocalDateTime changedSince, Collection<Long> currentIds);
}
