package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.ElementInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Verwaltung implements Element<Verwaltung, VerwaltungVersion> {
    private final ElementInfo<Verwaltung, VerwaltungVersion> elementInfo;
    private final String code;
    private final Betreiber betreiber;
    private final String infoPlusTc;
}
