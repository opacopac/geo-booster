package com.tschanz.geobooster.tarif.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class AwbIncVerwaltung {
    private final long id;
    private final long awbVersionId;
    private final long verwaltungId;
}
