package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;

import java.util.Map;


public interface TarifkantePersistenceRepo {
    Map<Long, Tarifkante> readAllElements(Map<Long, Haltestelle> haltestelleMap);

    Map<Long, TarifkanteVersion> readAllVersions(Map<Long, Tarifkante> elementMap);
}
