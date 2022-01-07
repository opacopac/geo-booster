package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.util.Collection;


public interface RgAuspraegungPersistence extends VersionedObjectPersistence<RgAuspraegung, RgAuspraegungVersion> {
    Collection<RgAuspraegungVersion> readAllVersions(Collection<RgAuspraegung> elements);
}
