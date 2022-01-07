package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;


public interface HaltestelleWegangabePersistence extends VersionedObjectPersistence<HaltestelleWegangabe, HaltestelleWegangabeVersion> {
}
