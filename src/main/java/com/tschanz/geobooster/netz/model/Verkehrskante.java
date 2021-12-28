package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Verkehrskante implements Element {
    private final long id;
    private final long haltestelle1Id;
    private final long haltestelle2Id;
}
