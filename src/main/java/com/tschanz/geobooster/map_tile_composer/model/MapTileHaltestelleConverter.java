package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.HaltestelleStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.model.WaHaltestelleStyle;
import com.tschanz.geobooster.map_tile.model.MapTilePoint;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileHaltestelleConverter {
    public MapTilePoint toMapTile(HaltestelleVersion hstV, float zoomLevel, Collection<MapStyle> mapStyles) {
        var style = mapStyles.contains(MapStyle.WEGANGABE_HALTESTELLEN)
            ? WaHaltestelleStyle.getStyle(zoomLevel)
            : HaltestelleStyle.getStyle(zoomLevel);

        return new MapTilePoint(
            CoordinateConverter.convertToEpsg3857(hstV.getCoordinate()),
            style
        );
    }
}
