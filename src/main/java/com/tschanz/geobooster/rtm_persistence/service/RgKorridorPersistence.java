package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.util.Collection;


public interface RgKorridorPersistence extends VersionedObjectPersistence<RgKorridor, RgKorridorVersion> {
    Collection<KeyValue<Long, Long>> readAllKorridorTkIds();
}
