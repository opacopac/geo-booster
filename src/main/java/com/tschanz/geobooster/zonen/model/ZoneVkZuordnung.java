package com.tschanz.geobooster.zonen.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ZoneVkZuordnung {
    private final long id;
    private final long zoneVersionId;
    private final long verkehrskanteId;
}
