package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;

import java.util.Map;


public interface BetreiberCacheRepo {
    void init();

    Map<Long, Betreiber> getElementMap();

    Map<Long, BetreiberVersion> getVersionMap();
}
