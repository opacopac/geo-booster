package com.tschanz.geobooster.maptile.service;

import com.tschanz.geobooster.graphics.model.GbImage;
import com.tschanz.geobooster.maptile.model.MapTile;


public interface MapTileService {
    GbImage renderTile(MapTile tile);
}
