package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.Verwaltung;

import java.util.Map;


public interface VerkehrskantePersistenceRepo {
    Map<Long, Verkehrskante> readAllElements(Map<Long, Haltestelle> haltestelleMap);

    Map<Long, VerkehrskanteVersion> readAllVersions(Map<Long, Verkehrskante> elementMap, Map<Long, Verwaltung> verwaltungMap);
}
