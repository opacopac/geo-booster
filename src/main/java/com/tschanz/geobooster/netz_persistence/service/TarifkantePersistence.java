package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.versioning_persistence.service.VersionedObjectPersistence;

import java.util.Collection;


public interface TarifkantePersistence extends VersionedObjectPersistence<Tarifkante, TarifkanteVersion> {
    Collection<Tarifkante> readElements(ReadFilter filter);

    Collection<TarifkanteVersion> readVersions(ReadFilter filter);
}
