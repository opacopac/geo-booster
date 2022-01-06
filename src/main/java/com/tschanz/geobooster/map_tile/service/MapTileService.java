package com.tschanz.geobooster.map_tile.service;

import com.tschanz.geobooster.graphics.model.GbImage;
import com.tschanz.geobooster.map_tile.model.MapTile;


public interface MapTileService {
    GbImage renderTile(MapTile tile);
}
