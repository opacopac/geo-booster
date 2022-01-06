package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;

import java.util.Collection;


public interface TarifkanteLayerService {
    Collection<TarifkanteVersion> searchObjects(MapLayerRequest request);
}
