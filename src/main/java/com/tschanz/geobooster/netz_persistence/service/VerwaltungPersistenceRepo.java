package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;

import java.util.Map;


public interface VerwaltungPersistenceRepo {
    Map<Long, Verwaltung> readAllElements(Map<Long, Betreiber> betreiberMap);

    Map<Long, VerwaltungVersion> readAllVersions(Map<Long, Verwaltung> elementMap);
}
