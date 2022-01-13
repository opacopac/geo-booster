package com.tschanz.geobooster.versioning_persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class ElementVersionChanges<E, V> {
    private final Collection<E> modifiedElements;
    private final Collection<V> modifiedVersions;
    private final Collection<Long> deletedElementIds;
    private final Collection<Long> deletedVersionIds;
}
