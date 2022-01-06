package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.map_layer.model.MapLayerResponse;


public interface MapLayerService {
    MapLayerResponse searchObjects(MapLayerRequest request);
}
