package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.VerkehrskanteLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;

import java.util.Collection;


public interface VerkehrskanteLayerService {
    Collection<VerkehrskanteVersion> searchObjects(VerkehrskanteLayerRequest request);
}
