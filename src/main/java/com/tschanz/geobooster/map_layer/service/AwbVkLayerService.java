package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbVkLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;

import java.util.Collection;


public interface AwbVkLayerService {
    Collection<VerkehrskanteVersion> searchObjects(AwbVkLayerRequest request);
}
