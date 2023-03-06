package com.tschanz.geobooster.versioning_repo.service;

import java.time.LocalDate;
import java.util.Collection;


public interface VersionedObjectRepo<E, V> {
    void loadData();

    void initRepo();

    E getElement(long id);

    V getVersion(long id);

    Collection<V> getElementVersions(long elementId);

    V getElementVersionAtDate(long elementId, LocalDate date);
}
