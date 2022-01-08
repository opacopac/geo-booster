package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.service.StyleHelper;
import com.tschanz.geobooster.map_tile.model.MapTilePoint;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileHaltestelleConverter {
    public MapTilePoint toMapTile(HaltestelleVersion hstV, float zoomLevel, Collection<MapStyle> mapStyles) {
        return new MapTilePoint(
            hstV.getCoordinate(),
            StyleHelper.getPointStyle(mapStyles, zoomLevel)
        );
    }
}
