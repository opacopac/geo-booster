package com.tschanz.geobooster.netz_persistence.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;

import java.util.Map;


public interface BetreiberPersistenceRepo {
    Map<Long, Betreiber> readAllElements();

    Map<Long, BetreiberVersion> readAllVersions(Map<Long, Betreiber> elementMap);
}
