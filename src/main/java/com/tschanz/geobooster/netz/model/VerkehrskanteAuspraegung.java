package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.ElementInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VerkehrskanteAuspraegung implements Element<VerkehrskanteAuspraegung, VerkehrskanteAuspraegungVersion> {
    private final ElementInfo<VerkehrskanteAuspraegung, VerkehrskanteAuspraegungVersion> elementInfo;
    private final Verkehrskante verkehrskante;
    private final Verwaltung verwaltung;
    private final VerkehrsmittelTyp verkehrsmittelTyp;
}
