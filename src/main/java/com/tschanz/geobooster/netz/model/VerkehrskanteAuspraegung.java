package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VerkehrskanteAuspraegung implements Element {
    private final long id;
    private final long verkehrskanteId;
    private final long verwaltungId;
    private final VerkehrsmittelTyp verkehrsmittelTyp;
}
