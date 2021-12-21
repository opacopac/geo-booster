package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;

import java.util.Map;


public interface HaltestellenPersistenceRepo {
    Map<Long, Haltestelle> readAllElements();

    Map<Long, HaltestelleVersion> readAllVersions(Map<Long, Haltestelle> elementMap);
}
