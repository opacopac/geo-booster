package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDateTime;
import java.util.Collection;


public interface RgAuspraegungPersistence extends VersionedObjectPersistence<RgAuspraegung, RgAuspraegungVersion> {
    ElementVersionChanges<RgAuspraegung, RgAuspraegungVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
