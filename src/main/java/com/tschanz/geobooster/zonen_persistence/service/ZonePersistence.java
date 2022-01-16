package com.tschanz.geobooster.zonen_persistence.service;

import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;

import java.time.LocalDateTime;
import java.util.Collection;


public interface ZonePersistence extends VersionedObjectPersistence<Zone, ZoneVersion> {
    Collection<Zone> readElements(Collection<Long> filterElementIds);

    Collection<ZoneVersion> readVersions(Collection<Long> filterVersionIds);

    ElementVersionChanges<Zone, ZoneVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
