package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.ElementInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Haltestelle implements Element<Haltestelle, HaltestelleVersion> {
    private final ElementInfo<Haltestelle, HaltestelleVersion> elementInfo;
    private final int uicCode;
}

