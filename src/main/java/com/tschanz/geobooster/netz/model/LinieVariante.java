package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class LinieVariante implements HasId {
    private final long id;
    private final Collection<Long> verkehrskanteIds;
}
