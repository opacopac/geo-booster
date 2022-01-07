package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.HaltestelleWegangabeLayerRequest;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;

import java.util.Collection;


public interface HaltestelleWegangabeLayerService {
    Collection<HaltestelleWegangabeVersion> searchObjects(HaltestelleWegangabeLayerRequest request);
}
