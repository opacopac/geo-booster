package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.ElementInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Betreiber implements Element<Betreiber, BetreiberVersion> {
    private final ElementInfo<Betreiber, BetreiberVersion> elementInfo;
    private final String name;
    private final String abkuerzung;
}
