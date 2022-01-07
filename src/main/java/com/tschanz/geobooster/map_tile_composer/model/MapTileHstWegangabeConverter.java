package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.HaltestelleStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.model.WaHaltestelleStyle;
import com.tschanz.geobooster.map_tile.model.MapTilePoint;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_repo.service.HaltestelleWegangabeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileHstWegangabeConverter {
    private final HaltestelleWegangabeRepo hstWegangabeRepo;


    public MapTilePoint toMapTile(HaltestelleWegangabeVersion hstWaV, float zoomLevel, Collection<MapStyle> mapStyles) {
        var style = mapStyles.contains(MapStyle.WEGANGABE_HALTESTELLEN)
            ? WaHaltestelleStyle.getStyle(zoomLevel)
            : HaltestelleStyle.getStyle(zoomLevel);

        var hstV = this.hstWegangabeRepo.getHaltestelleVersion(hstWaV);

        return new MapTilePoint(
            CoordinateConverter.convertToEpsg3857(hstV.getCoordinate()),
            style
        );
    }
}
