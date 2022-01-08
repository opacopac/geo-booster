package com.tschanz.geobooster.zonen_persistence.service;

import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;

import java.util.Collection;


public interface ZonenplanPersistence extends VersionedObjectPersistence<Zonenplan, ZonenplanVersion> {
    Collection<ZonenplanVersion> readAllVersions(Collection<Zonenplan> elements);
}
