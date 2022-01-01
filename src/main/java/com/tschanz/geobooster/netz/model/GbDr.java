package com.tschanz.geobooster.netz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;


@Getter
@Setter
@AllArgsConstructor
public class GbDr {
    private Collection<Betreiber> betreiber;
    private Collection<BetreiberVersion> betreiberVersions;
    private Collection<Verwaltung> verwaltungen;
    private Collection<VerwaltungVersion> verwaltungVersions;
    private Collection<Haltestelle> haltestellen;
    private Collection<HaltestelleVersion> haltestelleVersions;
    private Collection<Verkehrskante> verkehrskanten;
    private Collection<VerkehrskanteVersion> verkehrskanteVersions;
    private Collection<Tarifkante> tarifkanten;
    private Collection<TarifkanteVersion> tarifkanteVersions;


    public static GbDr createEmpty() {
        return new GbDr(
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }
}
