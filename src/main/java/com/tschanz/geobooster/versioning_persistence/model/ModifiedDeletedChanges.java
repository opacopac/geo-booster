package com.tschanz.geobooster.versioning_persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class ModifiedDeletedChanges {
    private final Collection<Long> modifiedIds;
    private final Collection<Long> deletedIds;
}
