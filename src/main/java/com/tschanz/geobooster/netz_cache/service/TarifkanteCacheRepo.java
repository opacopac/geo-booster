package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.service.TarifkanteRepo;

import java.util.Map;


public interface TarifkanteCacheRepo extends TarifkanteRepo {
    Map<Long, Tarifkante> getElementMap();

    Map<Long, TarifkanteVersion> getVersionMap();
}
