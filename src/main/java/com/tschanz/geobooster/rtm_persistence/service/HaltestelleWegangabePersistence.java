package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.time.LocalDateTime;
import java.util.Collection;


public interface HaltestelleWegangabePersistence extends VersionedObjectPersistence<HaltestelleWegangabe, HaltestelleWegangabeVersion> {
    ElementVersionChanges<HaltestelleWegangabe, HaltestelleWegangabeVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds);
}
