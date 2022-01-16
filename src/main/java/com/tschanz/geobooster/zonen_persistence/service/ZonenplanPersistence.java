package com.tschanz.geobooster.zonen_persistence.service;

import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;

import java.time.LocalDateTime;
import java.util.Collection;


public interface ZonenplanPersistence extends VersionedObjectPersistence<Zonenplan, ZonenplanVersion> {
    ElementVersionChanges<Zonenplan, ZonenplanVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
