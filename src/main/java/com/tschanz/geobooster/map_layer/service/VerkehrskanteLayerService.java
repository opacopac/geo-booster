package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;

import java.util.Collection;


public interface VerkehrskanteLayerService {
    Collection<VerkehrskanteVersion> searchObjects(MapLayerRequest request);
}
