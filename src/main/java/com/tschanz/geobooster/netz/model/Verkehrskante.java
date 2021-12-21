package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.ElementInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Verkehrskante implements Element<Verkehrskante, VerkehrskanteVersion> {
    private final ElementInfo<Verkehrskante, VerkehrskanteVersion> elementInfo;
    private final Haltestelle haltestelle1;
    private final Haltestelle haltestelle2;
}
