package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.UnmappedTarifkanteLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;

import java.util.Collection;


public interface UnmappedTarifkanteLayerService {
    Collection<TarifkanteVersion> searchObjects(UnmappedTarifkanteLayerRequest request);
}
