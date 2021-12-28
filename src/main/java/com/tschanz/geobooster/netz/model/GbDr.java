package com.tschanz.geobooster.netz.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class GbDr {
    private final Collection<Betreiber> betreiber;
    private final Collection<BetreiberVersion> betreiberVersions;
    private final Collection<Verwaltung> verwaltungen;
    private final Collection<VerwaltungVersion> verwaltungVersions;
    private final Collection<Haltestelle> haltestellen;
    private final Collection<HaltestelleVersion> haltestelleVersions;
    private final Collection<Verkehrskante> verkehrskanten;
    private final Collection<VerkehrskanteVersion> verkehrskanteVersions;
    private final Collection<Tarifkante> tarifkanten;
    private final Collection<TarifkanteVersion> tarifkanteVersions;
}
