package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;

import java.util.Map;


public interface HaltestelleCacheRepo extends HaltestelleRepo {
    Map<Long, Haltestelle> getElementMap();

    Map<Long, HaltestelleVersion> getVersionMap();
}
