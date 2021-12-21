package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.ElementInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Tarifkante implements Element<Tarifkante, TarifkanteVersion> {
    private final ElementInfo<Tarifkante, TarifkanteVersion> elementInfo;
    private final Haltestelle haltestelle1;
    private final Haltestelle haltestelle2;
}
