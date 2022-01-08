package com.tschanz.geobooster.zonen_persistence.service;

import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;

import java.util.Collection;


public interface ZonePersistence extends VersionedObjectPersistence<Zone, ZoneVersion> {
    Collection<KeyValue<Long, Long>> readAllVkIds();
}
