package com.tschanz.geobooster.versioning.service;

import java.time.LocalDate;
import java.util.Collection;


public interface VersionedObjectRepo<E, V> {
    void init(Collection<E> elements, Collection<V> versions);

    E getElement(long id);

    V getVersion(long id);

    Collection<V> getElementVersions(long elementId);

    V getElementVersionAtDate(long elementId, LocalDate date);
}
