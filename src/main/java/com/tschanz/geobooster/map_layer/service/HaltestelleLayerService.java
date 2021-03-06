package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.HaltestelleLayerRequest;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;

import java.util.Collection;


public interface HaltestelleLayerService {
    Collection<HaltestelleVersion> searchObjects(HaltestelleLayerRequest request);
}
