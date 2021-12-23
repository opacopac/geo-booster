package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;

import java.util.Map;


public interface VerwaltungCacheRepo {
    Map<Long, Verwaltung> getElementMap();

    Map<Long, VerwaltungVersion> getVersionMap();
}
