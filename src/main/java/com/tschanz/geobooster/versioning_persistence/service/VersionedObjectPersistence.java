package com.tschanz.geobooster.versioning_persistence.service;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.Version;

import java.util.Collection;


public interface VersionedObjectPersistence<E extends Element, V extends Version> {
    Collection<E> readAllElements();

    Collection<V> readAllVersions();
}
