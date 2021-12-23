package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.Verwaltung;

import java.util.Map;


public interface VerkehrskanteAuspraegungPersistenceRepo {
    Map<Long, VerkehrskanteAuspraegung> readAllElements(Map<Long, Verkehrskante> verkehrskanteMap, Map<Long, Verwaltung> verwaltungMap);

    Map<Long, VerkehrskanteAuspraegungVersion> readAllVersions(Map<Long, VerkehrskanteAuspraegung> elementMap);
}
