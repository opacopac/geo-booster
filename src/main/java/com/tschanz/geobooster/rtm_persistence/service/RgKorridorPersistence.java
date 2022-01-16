package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDateTime;
import java.util.Collection;


public interface RgKorridorPersistence extends VersionedObjectPersistence<RgKorridor, RgKorridorVersion> {
    ElementVersionChanges<RgKorridor, RgKorridorVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
