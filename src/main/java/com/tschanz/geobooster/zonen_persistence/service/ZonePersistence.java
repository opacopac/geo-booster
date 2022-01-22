package com.tschanz.geobooster.zonen_persistence.service;

import com.tschanz.geobooster.util.model.Tuple2;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen.model.ZoneVkZuordnung;

import java.time.LocalDateTime;
import java.util.Collection;


public interface ZonePersistence extends VersionedObjectPersistence<Zone, ZoneVersion> {
    Collection<ZoneVkZuordnung> readAllZoneVkZuordnung();

    ElementVersionChanges<Zone, ZoneVersion> findZoneVersionChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);

    Tuple2<Collection<ZoneVkZuordnung>, Collection<Long>> findZoneVkZuordnungChanges(LocalDateTime changedSince, Collection<Long> currentIds);
}
