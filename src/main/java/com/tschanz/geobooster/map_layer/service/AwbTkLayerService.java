package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbTkLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;

import java.util.Collection;


public interface AwbTkLayerService {
    Collection<TarifkanteVersion> searchObjects(AwbTkLayerRequest request);
}
