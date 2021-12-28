package com.tschanz.geobooster.versioning.service;

import java.util.Collection;


public interface VersionedObjectPersistence<E, V> {
    Collection<E> readAllElements();

    Collection<V> readAllVersions();
}
