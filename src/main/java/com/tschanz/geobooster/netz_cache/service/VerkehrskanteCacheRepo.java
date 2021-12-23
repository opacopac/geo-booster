package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;

import java.util.Map;


public interface VerkehrskanteCacheRepo extends VerkehrskanteRepo {
    Map<Long, Verkehrskante> getElementMap();

    Map<Long, VerkehrskanteVersion> getVersionMap();
}
